create database order_item_db;
use order_item_db;

create table order_item_details(
	item_id int primary key auto_increment,
    order_id int,
    catalogue_id int,
    tailor_id int,
    item_status varchar(20));
