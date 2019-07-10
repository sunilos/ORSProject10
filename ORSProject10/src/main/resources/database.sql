CREATE TABLE rt_role (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_datetime datetime(6) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_datetime datetime(6) DEFAULT NULL,
  org_id bigint(20) DEFAULT NULL,
  discription varchar(100) DEFAULT NULL,
  name varchar(50) DEFAULT NULL,
  org_name varchar(50) DEFAULT NULL,
  can_delete varchar(1) DEFAULT NULL,
  can_read varchar(1) DEFAULT NULL,
  can_update varchar(1) DEFAULT NULL,
  can_write varchar(1) DEFAULT NULL,
  status varchar(15) DEFAULT NULL,
  status1 varchar(15) DEFAULT NULL,
  PRIMARY KEY (id)
) 

INSERT INTO rt_role (name,discription,can_delete,can_read,can_update,can_write,status,org_id,org_name) VALUES ('Admin','Administrator','Y','Y','Y','Y','Active','0','root')
INSERT INTO rt_role (name,discription,can_delete,can_read,can_update,can_write,status,org_id,org_name) VALUES ('Superadmin','Super Administrator','Y','Y','Y','Y','Active','0','root')


CREATE TABLE rt_user (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_by varchar(50) DEFAULT NULL,
  created_datetime datetime(6) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_datetime datetime(6) DEFAULT NULL,
  org_id bigint(20) DEFAULT NULL,
  alternate_mobile varchar(50) DEFAULT NULL,
  dob datetime(6) DEFAULT NULL,
  email varchar(50) DEFAULT NULL,
  first_name varchar(50) DEFAULT NULL,
  gender varchar(10) DEFAULT NULL,
  image_path varchar(250) DEFAULT NULL,
  last_name varchar(50) DEFAULT NULL,
  login_id varchar(50) DEFAULT NULL,
  password varchar(50) DEFAULT NULL,
  phone varchar(50) DEFAULT NULL,
  role_id bigint(20) DEFAULT NULL,
  role_name varchar(50) DEFAULT NULL,
  status varchar(20) DEFAULT NULL,
  org_name varchar(50) DEFAULT NULL,
  access_time_from time DEFAULT NULL,
  access_time_to time DEFAULT NULL,
  image_id bigint(20) DEFAULT NULL,
  last_login datetime(6) DEFAULT NULL,
  unsuccess_login int(11) DEFAULT NULL,
  valid_from_date datetime(6) DEFAULT NULL,
  valid_to_date datetime(6) DEFAULT NULL,
  PRIMARY KEY (id)
) 

CREATE TABLE rt_message (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  code varchar(100) NOT NULL,
  subject varchar(200) NOT NULL,
  body longtext NOT NULL,
  is_html varchar(1) DEFAULT NULL,
  type varchar(15) DEFAULT NULL,
  status varchar(15) DEFAULT NULL,
  created_by varchar(50) DEFAULT NULL,
  created_datetime datetime(6) DEFAULT NULL,
  modified_by varchar(50) DEFAULT NULL,
  modified_datetime datetime(6) DEFAULT NULL,
  org_id bigint(20) DEFAULT NULL,
  org_name varchar(50) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_e4i750et12ng5nv3t9m46v1pt (code)
) 

INSERT INTO rt_message (code,subject,body,is_html,type,status) VALUES ('U-REG','{user} has been registered','<H1>Congratulation {user}</H1> Login {login}<br> Password {password}','Y','EMAIL','Active');
INSERT INTO rt_message (code,subject,body,is_html,type,status) VALUES ('U-FP','{user} password','<H1>Dear {user}</H1> Your password is {password}','Y','EMAIL','Active');
INSERT INTO rt_message (code,subject,body,is_html,type,status) VALUES ('U-CP','{user} password is changed','<H1>Dear {user}</H1> Your password is is successfully changed','Y','EMAIL','Active');


CREATE TABLE rt_student (
  id bigint(20) NOT NULL auto_increment,
  created_by varchar(50) default NULL,
  created_datetime datetime default NULL,
  modified_by varchar(50) default NULL,
  modified_datetime datetime default NULL,
  org_id bigint(20) default NULL,
  org_name varchar(50) default NULL,
  college_id bigint(20) default NULL,
  college_name varchar(50) default NULL,
  dob datetime default NULL,
  email varchar(50) default NULL,
  enrol_no varchar(20) default NULL,
  first_name varchar(50) default NULL,
  last_name varchar(50) default NULL,
  mobile_no varchar(15) default NULL,
  PRIMARY KEY  (id)
) 



CREATE TABLE rt_college (
  id bigint(20) NOT NULL auto_increment,
  created_by varchar(50) default NULL,
  created_datetime datetime default NULL,
  modified_by varchar(50) default NULL,
  modified_datetime datetime default NULL,
  org_id bigint(20) default NULL,
  org_name varchar(50) default NULL,
  address varchar(50) default NULL,
  city varchar(50) default NULL,
  name varchar(50) default NULL,
  phoneno varchar(15) default NULL,
  state varchar(50) default NULL,
  PRIMARY KEY  (id)
) 
