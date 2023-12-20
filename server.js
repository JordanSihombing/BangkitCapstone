const express = require('express');
const session = require('cookie-session');
const cors = require('cors');

const app = express();

app.use(cors());
app.use(session({ secret: 'your-secret-key' }));
app.use(express.json());
const db = require("../api/models");
const Role = db.role;
db.sequelize.sync({force: true}).then(() => {
  console.log('Drop and Resync Db');
  initial();
});

app.get('/', (req, res) => {
  res.json({ message: 'Welcome to the API.' });
});

app.listen(8080, () => {
  console.log('Server is running on port 8080.');
});

function initial() {
    Role.create({   
      id: 1,
      name: "user"
    });
   
    Role.create({
      id: 2,
      name: "moderator"
    });
   
    Role.create({
      id: 3,
      name: "admin"
    });
  }

  // routes
require('../api/routes/auth.routes')(app);
require('../api/routes/user.routes')(app);
// set port, listen for requests