const express = require('express');
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { Sequelize, DataTypes } = require('sequelize');

const app = express();
app.use(bodyParser.json());

// Database configuration
const sequelize = new Sequelize('your_dbname', 'your_username', 'your_password', {
  host: 'your_host',
  dialect: 'postgres',
});

// Define User and FitnessPlan models
const User = sequelize.define('User', {
  user_id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  },
  username: {
    type: DataTypes.STRING,
    unique: true,
    allowNull: false,
  },
  password_hash: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  height: DataTypes.INTEGER,
  weight: DataTypes.INTEGER,
  age: DataTypes.INTEGER,
  gender: DataTypes.STRING,
});

const FitnessPlan = sequelize.define('FitnessPlan', {
  plan_id: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  },
  workouts: DataTypes.JSON,
  nutrition: DataTypes.JSON,
});

// Define associations between User and FitnessPlan
User.hasOne(FitnessPlan, { foreignKey: 'user_id' });
FitnessPlan.belongsTo(User, { foreignKey: 'user_id' });

// Middleware for Token Authentication
function tokenRequired(req, res, next) {
  const token = req.headers.authorization;
  if (!token) {
    return res.status(401).json({ message: 'Token is missing!' });
  }

  try {
    const decoded = jwt.verify(token.split(" ")[1], 'your_secret_key');
    req.user = decoded;
    next();
  } catch (error) {
    return res.status(401).json({ message: 'Token is invalid!' });
  }
}

// Routes
app.post('/api/users', async (req, res) => {
  const data = req.body;
  const hashedPassword = await bcrypt.hash(data.password, 10);

  try {
    const newUser = await User.create({
      username: data.username,
      password_hash: hashedPassword,
      height: data.height,
      weight: data.weight,
      age: data.age,
      gender: data.gender,
    });

    return res.json(newUser);
  } catch (error) {
    return res.status(500).json({ message: 'Error registering user' });
  }
});

app.post('/api/login', async (req, res) => {
  const auth = req.body;

  try {
    const user = await User.findOne({
      where: { username: auth.username },
    });

    if (user && await bcrypt.compare(auth.password, user.password_hash)) {
      const token = jwt.sign({ user_id: user.user_id }, 'your_secret_key', { expiresIn: '1d' });
      return res.json({ token });
    }

    return res.status(401).json({ message: 'Invalid username or password' });
  } catch (error) {
    return res.status(500).json({ message: 'Error during login' });
  }
});

app.get('/api/users/:user_id', tokenRequired, async (req, res) => {
  const { user_id } = req.params;

  if (req.user.user_id != user_id) {
    return res.status(403).json({ message: 'Unauthorized access' });
  }

  try {
    const user = await User.findByPk(user_id, {
      include: [FitnessPlan],
    });

    return res.json(user);
  } catch (error) {
    return res.status(500).json({ message: 'Error retrieving user' });
  }
});

app.put('/api/users/:user_id', tokenRequired, async (req, res) => {
  const { user_id } = req.params;

  if (req.user.user_id != user_id) {
    return res.status(403).json({ message: 'Unauthorized access' });
  }

  try {
    const user = await User.findByPk(user_id);

    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    const data = req.body;
    user.height = data.height || user.height;
    user.weight = data.weight || user.weight;

    await user.save();

    return res.json(user);
  } catch (error) {
    return res.status(500).json({ message: 'Error updating user profile' });
  }
});

// Sync the database and start the server
sequelize.sync().then(() => {
  app.listen(3000, () => {
    console.log('Server is running on http://localhost:3000');
  });
});
