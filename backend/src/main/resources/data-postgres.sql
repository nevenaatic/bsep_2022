insert into app_user (id, address, city, country, email, name, password, role, surname) values (1, 'Lilie Bem', 'Novi Sad', 'Srbija', 'nevena@hotmail.com', 'Nevena', '12345', 'admin', 'Atic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (2, 'Pavla Papa', 'Novi Sad', 'Srbija', 'pavle@hotmail.com', 'Pavle', '12345', 'admin', 'Misic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (3, 'Lilie Bem', 'Novi Sad', 'Srbija', 'jovan@hotmail.com', 'Jovan', '12345', 'admin', 'Cvijic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (4, 'Lilie Bem', 'Novi Sad', 'Srbija', 'nikola@hotmail.com', 'Nikola', '12345', 'admin', 'Mijatovic');

insert into app_user (id, address, city, country, email, name, password, role, surname) values (5, 'Lilie Bem', 'Novi Sad', 'Srbija', 'mitaa@hotmail.com', 'Mita', '12345', 'certification_authority', 'Lahic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (6, 'Pavla Papa', 'Novi Sad', 'Srbija', 'ivan@hotmail.com', 'Ivan', '12345', 'certification_authority', 'Majkic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (7, 'Lilie Bem', 'Novi Sad', 'Srbija', 'steva@hotmail.com', 'Steva', '12345', 'certification_authority', 'Bakic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (8, 'Lilie Bem', 'Novi Sad', 'Srbija', 'paja@hotmail.com', 'Paja', '12345', 'certification_authority', 'Rokic');

insert into app_user (id, address, city, country, email, name, password, role, surname) values (9, 'Lilie Bem', 'Novi Sad', 'Srbija', 'mica@hotmail.com', 'Mica', '12345', 'end_user', 'Kosic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (10, 'Pavla Papa', 'Novi Sad', 'Srbija', 'ana@hotmail.com', 'Ana', '12345', 'end_user', 'Bekic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (11, 'Lilie Bem', 'Novi Sad', 'Srbija', 'misa@hotmail.com', 'Misa', '12345', 'end_user', 'Mitrovic');
insert into app_user (id, address, city, country, email, name, password, role, surname) values (12, 'Lilie Bem', 'Novi Sad', 'Srbija', 'aca@hotmail.com', 'Aca', '12345', 'end_user', 'Markovic');

insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(1, '11111111', 1, 1, 'aaaaaaaa', 'aaaaaaaa', '2022-04-10', '2030-12-12', false, 'CA');
insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(2, '22222222', 5, 1, 'bbbbbbbb', 'aaaaaaaa', '2022-04-10', '2030-12-12', false, 'CA');
insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(3, '33333333', 6, 1, 'cccccccc', 'aaaaaaaa', '2022-04-10', '2030-12-12', false, 'CA');
insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(4, '44444444', 9, 1, 'dddddddd', 'aaaaaaaa', '2022-04-10', '2030-12-12', false, 'end_entity');
insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(5, '55555555', 10,1, 'eeeeeeee', 'aaaaaaaa', '2022-04-10', '2030-12-12', false, 'end_entity');
insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(6, '66666666', 9, 5, 'ffffffff', 'bbbbbbbb', '2022-04-10', '2030-12-12', false, 'end_entity');
insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(7, '77777777', 7, 5, 'gggggggg', 'bbbbbbbb', '2022-04-10', '2030-12-12', false, 'CA');
insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(8, '88888888', 9, 6, 'hhhhhhhh', 'cccccccc', '2022-04-10', '2030-12-12', false, 'end_entity');
insert into certificate_data (id, serial_code, subject_user_id, issuer_user_id, subject_key_id, issuer_key_id, valid_from, valid_until, revoked, certificate_type) values 
(9, '99999999', 10,7, 'iiiiiiii', 'gggggggg', '2022-04-10', '2030-12-12', false, 'end_entity');
