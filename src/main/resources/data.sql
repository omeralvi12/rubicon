DROP TABLE IF EXISTS rubicon_orders;
 
CREATE TABLE rubicon_orders (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  farm_id VARCHAR(250) NOT NULL,
  start_date_time TIMESTAMP NOT NULL,
  duration INT NOT NULL,
  status VARCHAR(100) NOT NULL
);
