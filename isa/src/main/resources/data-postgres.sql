insert into address (country, place_name, street, zip_code, lon, lat)
    values ('Srbija', 'Sabac', 'Karadjordjeva 26', '15000', null, null ),
            ('Srbija', 'Novi Sad', 'Zeleznicka 36', '21000', null, null),
            ('Srbija', 'Novi Sad', 'Sonje Marinkovic 5', '21000', null, null),
            ('Srbija', 'Novi Sad', 'Bulevar Oslobodjenja 55', '21000', '19.83383399956332', '45.25697997579121'),
            ('Srbija', 'Novi Sad', 'Vase Pelagic 1', '21000', '19.840324439532647', '45.251395995531226'),
            ('Srbija', 'Sremska Kamenica', 'Karadjordjeva 33', '21000', '19.835541721737165', '45.22357844341107'),
            ('Srbija', 'Kula', 'Marsala Tita 200', '25230', null, null),
            ('Srbija', 'Zlatibor', 'Milenko Zablacanski', '31315', '19.652715410573126', '43.66153258311601'),
           ('Srbija', 'Novi Sad', 'Partizanski put 2', '21000', '19.819806290382964', ' 45.15281270382178');

insert into photo (photo_path, rental_id)
    values ('default.jpg', null),
            ('logo.png', null),
           ('cottage2.jpg', 4),
           ('cottage-inside.jpg', 4),
           ('cottage1.jpg', 5);

insert into admin (id, email, password, name, surname, address_id, phone_number, is_deleted, loyalty_points,
                        is_active, photo_id, user_type, loyalty_type)
    values (nextval('my_seq_gen_user'), 'admin1@gmail.com', 'admin123', 'Miroslav', 'Ilic', 2, '0614264444', false, 100,
                true, 2, 1, 0),
           (nextval('my_seq_gen_user'), 'admin2@gmail.com', 'admin123', 'Saban', 'Saulic', 3, '0614283764', false,100,
                true, 2, 1, 0);

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
                false, 8, 0, 0, 0, 90, 'wifi,parking,internet,bazen', 3, 5),
           (nextval('my_seq_gen_rental'), 'Fruskogorska zora', 'Najlepsa vikendica u okolini NS.', 5, 'Zabranjeno pusenje!',
            false, 9, 0, 0, 0, 100, 'wifi,klima,parking,rostilj', 4, 5);

insert into loyalty_program (discount, increase, loyalty_type)
    values (0, 0, 0),
            (10, 10, 1),
            (20, 20, 2),
            (30, 30, 3);

insert into fishing_instructor(id, email, is_active, is_deleted, loyalty_type, name, password, phone_number, surname,
                                user_type, address_id, photo_id, loyalty_points)
    values (nextval('my_seq_gen_user'), 'instructor@gmail.com', true, false, 0, 'Instro', 'sifra123', '0613222126',
                'Instric', 4, 5, 1, 0),
           (nextval('my_seq_gen_user'), 'instructor2@gmail.com', true, false, 0, 'Instro2', 'sifra1223', '0619990009',
        'Instriiiic', 4, 3, 1, 0);

insert into adventure (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                       rental_type, price, biography, fishing_equipment, cancellation_conditions, instructor_id)
values (nextval('my_seq_gen_rental'), 'Najjakija Avantura', 'Ide se na svakakva mesta, lepo skroz.', 10, 'Mora budete dobri, da se postujete i tako to.',
            false, 4, 0, 0, 2, 30, 'Skroz sam jak lik, ko avantura.', 'Pecaljka i tjt.', 10.5, 6),
       (nextval('my_seq_gen_rental'), 'Pecaj pecaj pecaj', 'Peca se ceo dan sta da kazem.', 15, 'Kako ja kazem tako se radi.',
            false, 5, 0, 0, 2, 25, 'Biografija, ne znam sta da napisem.', 'Sve je obezbedjeno.', 20.0, 6);

insert into request(id, is_answered, message, sent_time, request_type, sender_id)
values (nextval('my_seq_gen_notification'), false, 'zeleo bih da mi se profil obrise iz razloga 111111111', '02/05/2022 23:44', 1, 6),
       (nextval('my_seq_gen_notification'), false, 'zeleo bih da mi se profil obrise iz razloga 222222222', '02/05/2022 13:14', 1, 7);



