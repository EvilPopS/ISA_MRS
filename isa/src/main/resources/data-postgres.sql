insert into address (place_name, street, zip_code)
    values ('Sabac', 'Karadjordjeva 26', '15000'),
            ('Novi Sad', 'Zeleznicka 36', '21000'),
            ('Novi Sad', 'Sonje Marinkovic 5', '21000'),
            ('Novi Sad', 'Bulevar Oslobodjenja 55', '21000'),
            ('Novi Sad', 'Vase Pelagic 1', '21000'),
            ('Sremska Kamenica', 'Ulica Heroja 2', '21000'),
            ('Kula', 'Marsala Tita 200', '25230'),
            ('Zlatibor', 'Milenko Zablacanski', '31315');

insert into photo (photo_path, rental_id)
    values ('default.jpg', null),
            ('logo.png', null),
           ('cottage2.jpg', 4),
           ('cottage-inside.jpg', 4),
           ('cottage1.jpg', 5);

insert into admin (id, email, password, name, surname, address_id, phone_number, is_deleted,
                        is_active, photo_id, user_type, loyalty_type)
    values (nextval('my_seq_gen_user'), 'admin1@gmail.com', 'admin123', 'Miroslav', 'Ilic', 2, '0614264444', false,
                true, null, 1, 0),
           (nextval('my_seq_gen_user'), 'admin2@gmail.com', 'admin123', 'Saban', 'Saulic', 3, '0614283764', false,
                true, null, 1, 0);

insert into adventure (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                            rental_type, price, biography, fishing_equipment, cancellation_conditions)
    values (nextval('my_seq_gen_rental'), 'Najjakija Avantura', 'Ide se na svakakva mesta, lepo skroz.', 10, 'Mora budete dobri, da se postujete i tako to.',
                false, 4, 0, 0, 2, 30, 'Skroz sam jak lik, ko avantura.', 'Pecaljka i tjt.', 10.5),
            (nextval('my_seq_gen_rental'), 'Pecaj pecaj pecaj', 'Peca se ceo dan sta da kazem.', 15, 'Kako ja kazem tako se radi.',
                false, 5, 0, 0, 2, 25, 'Biografija, ne znam sta da napisem.', 'Sve je obezbedjeno.', 20.0);

insert into boat (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                      rental_type, price, type, boat_length, engine_number, engine_power, max_speed,
                      navigation_equipment, fishing_equipment, cancellation_conditions)
    values (nextval('my_seq_gen_rental'), 'The Black Pearl', 'Crni brodic, velik i lep.', 8, 'Nema skakanja sa brodica.', false, 6, 0, 0, 1, 50,
                'Ribarski brodic', '20m', '46345754', '500ks', '30km/h', 'Neka navigaciona oprema ne razumem ti se ja u to.',
                'Pecaroska oprema, ne znam sta sve ide tu.', 35.0);

insert into boat_owner (id, email, password, name, surname, address_id, phone_number, is_deleted,
                            is_active, photo_id, user_type, loyalty_type, loyalty_points)
    values (nextval('my_seq_gen_user'), 'boatowner1@gmail.com', 'boat123', 'Bojan', 'Bojanic', 7, '06222332323', false, true, null,
                3, 0, 10);

insert into client (id, email, password, name, surname, address_id, phone_number, is_deleted,
                        is_active, photo_id, user_type, loyalty_type, no_penalties, loyalty_points)
    values (nextval('my_seq_gen_user'), 'strahinjapopovic@gmail.com', 'sifra123', 'Strahinja', 'Popovic', 1, '0601231231', false,
                true, 1, 0, 0, 0, 124);

insert into cottage_owner (id, email, password, name, surname, address_id, phone_number, is_deleted,
                           is_active, photo_id, user_type, loyalty_type, loyalty_points)
values (nextval('my_seq_gen_user'), 'srdjan@gmail.com', 'srdja123@', 'Srdjan', 'Djuric', 1, '06222602323', false, true, 2,
        2, 0, 10);

insert into cottage (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                       rental_type, price, additional_services, no_rooms,cottage_owner_id)
    values (nextval('my_seq_gen_rental'), 'Vikendica Drvence', 'Lepa mala drvena vikendica na brdu.', 5, 'Nema lomljenja staklenih predmeta!',
                false, 8, 0, 0, 1, 90, 'wifi,parking,internet,bazen', 3, 5),
           (nextval('my_seq_gen_rental'), 'Fruskogorska zora', 'Najlepsa vikendica u okolini NS.', 5, 'Zabranjeno pusenje!',
            false, 6, 0, 0, 1, 100, 'wifi,klima,parking,rostilj', 4, 5);

insert into loyalty_program (discount, increase, loyalty_type)
    values (0, 0, 0),
            (10, 10, 1),
            (20, 20, 2),
            (30, 30, 3);

insert into fishing_instructor(id, email, is_active, is_deleted, loyalty_type, name, password, phone_number, surname, user_type, address_id, photo_id, loyalty_points) values
    (nextval('my_seq_gen_user'), 'instructor@gmail.com', true, false, 0, 'Instro', 'sifra123', '0613222126', 'Instric', 4, 5, 1, 0);


insert into reservation (start_time, end_time, is_action, is_reserved, price, rental_id)
    values ('11/4/2022', '29/4/2022', false, true, 30, 1),
            ('1/5/2022', '11/5/2022', false, true, 30, 1),
            ('17/4/2022', '3/5/2022', false, true, 25, 2),
            ('21/4/2022', '2/5/2022', false, true, 25, 2),
            ('3/4/2022', '23/4/2022', false, true, 50, 3),
            ('1/6/2022', '12/6/2022', false, true, 50, 3),
            ('21/4/2022', '2/5/2022', false, true, 50, 3),
            ('12/4/2022', '29/4/2022', false, true, 50, 4),
            ('10/5/2022', '29/5/2022', false, true, 50, 4)
