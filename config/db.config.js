module.exports = {
    HOST: "34.101.50.117",
    USER: "root",
    PASSWORD: "bbadmin",
    DB: "userdata",
    dialect: "mysql",
    port: 3306,
    pool: {
      max: 5,
      min: 0,
      acquire: 30000,
      idle: 10000
    }
  };
  