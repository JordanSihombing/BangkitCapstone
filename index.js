// app.js
const express = require('express');
const bodyParser = require('body-parser');
const mysql = require('mysql');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const config = require('./config');

const app = express();
const port = 8080;

// Middleware
app.use(bodyParser.json());

// Database connection
const db = mysql.createConnection(config.db);

db.connect((err) => {
    if (err) {
        console.error('Error connecting to MySQL:', err);
    } else {
        console.log('Connected to MySQL');
    }
});

const checkUsername = (username) => {
    return new Promise((resolve, reject) => {
        const query = 'SELECT * FROM users WHERE username = ?';
        db.query(query, [username], (err, result) => {
            if (err) {
                reject(err);
            } else if (result.length > 0) {
                reject('Username already in use');
            } else {
                resolve();
            }
        });
    });
};

// Hash password function
const hashPassword = (password) => {
    return bcrypt.hashSync(password, 10);
};

// Routes
app.post('/register', async (req, res) => {
    const { username, password } = req.body;

    try {
        await checkUsername(username);
    } catch (err) {
        return res.status(400).json({ error: err.message });
    }

    // Hash the password
    const hashedPassword = hashPassword(password);

    // Save the user to the database
    const query = 'INSERT INTO users (username, password) VALUES (?, ?)';
    db.query(query, [username, hashedPassword], (err, result) => {
        if (err) {
            console.error('Error registering user:', err);
            res.status(500).json({ error: 'Internal Server Error' });
        } else {
            res.status(201).json({ message: 'User registered successfully' });
        }
    });
});

app.post('/login', (req, res) => {
    const { username, password } = req.body;


    const query = 'SELECT * FROM users WHERE username = ?';
    db.query(query, [username], (err, result) => {
        if (err) {
            console.error('Error logging in:', err);
            res.status(500).json({ error: 'Internal Server Error' });
        } else if (result.length === 0) {
            res.status(401).json({ error: 'Username or Password is incorrect' });
        } else {
            // Check hashed password
            const hashedPassword = result[0].password;
            if (bcrypt.compareSync(password, hashedPassword)) {
                // Passwords match, generate JWT token
                const token = jwt.sign({ username }, config.jwtSecret, { expiresIn: '1h' });
                res.json({ token });
            } else {
                res.status(401).json({ error: 'Username or Password is incorrect' });
            }
        }
    });
});

// Middleware to verify JWT token
const verifyToken = (req, res, next) => {
    const token = req.header('Authorization');

    if (!token) {
        return res.status(401).json({ error: 'Unauthorized: No token provided' });
    }

    jwt.verify(token.replace('Bearer ', ''), config.jwtSecret, (err, decoded) => {
        if (err) {
            return res.status(401).json({ error: 'Unauthorized: Invalid token' });
        }

        req.user = decoded;
        next();
    });
};

// Example usage in a protected route
app.get('/protected', verifyToken, (req, res) => {
    res.json({ message: 'Protected endpoint accessed successfully' });
});

// Start the server
app.listen(port, () => {
    console.log(`Server is running on port http://localhost:${port}`);
});
