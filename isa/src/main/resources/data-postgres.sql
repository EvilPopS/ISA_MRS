set datestyle to European;
INSERT INTO role (name) VALUES ('ROLE_CLIENT'),
                                ('ROLE_COTTAGE_OWNER'),
                                ('ROLE_BOAT_OWNER'),
                                ('ROLE_INSTRUCTOR'),
                                ('ROLE_ADMIN');


insert into address (country, place_name, street, lon, lat)
    values ('Srbija', 'Sabac', 'Karadjordjeva 26', null, null ),
            ('Srbija', 'Novi Sad', 'Zeleznicka 36', null, null),
            ('Srbija', 'Novi Sad', 'Sonje Marinkovic 5', null, null),
            ('Srbija', 'Novi Sad', 'Bulevar Oslobodjenja 55', '19.83383399956332', '45.25697997579121'),
            ('Srbija', 'Novi Sad', 'Vase Pelagic 1', '19.840324439532647', '45.251395995531226'),
            ('Srbija', 'Sremska Kamenica', 'Karadjordjeva 33', '19.835541721737165', '45.22357844341107'),
            ('Srbija', 'Kula', 'Marsala Tita 200', null, null),
            ('Srbija', 'Zlatibor', 'Milenko Zablacanski', '19.652715410573126', '43.66153258311601'),
           ('Srbija', 'Novi Sad', 'Partizanski put 2', '19.819806290382964', ' 45.15281270382178'),
           ('Srbija', 'Novi Sad', 'Bulevar Oslobodjenja 55', '19.83383399956332', '45.25697997579121');

insert into photo (photo_path, rental_id)
    values ('default.jpg', null),
            ('logo.png', null),
           ('cottage2.jpg', 4),
           ('cottage-inside.jpg', 4),
           ('cottage1.jpg', 5),
           ('cottage1.jpg', 6),
           ('fishing_adventure1.jpg', 7),
           ('fishing_adventure2.jpg', 8),
           ('home-page-pic-boat.jpg', 1),
           ('home-page-pic-boat.jpg', 2);

insert into admin (id, email, password, name, surname, address_id, phone_number, is_deleted, loyalty_points,
                        is_active, photo_id, role_id, loyalty_type)
    values (nextval('my_seq_gen_user'), 'admin1@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', 'Miroslav', 'Ilic', 2, '0614264444', false, 100,
                true, 2, 5, 0),
           (nextval('my_seq_gen_user'), 'admin2@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', 'Saban', 'Saulic', 3, '0614283764', false,100,
                true, 2, 5, 0);

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
                            is_active, photo_id, role_id, loyalty_type, loyalty_points)
    values (nextval('my_seq_gen_user'), 'boatowner1@gmail.com', 'boat123', 'Bojan', 'Bojanic', 7, '06222332323', false, true, null,
                3, 0, 10);

insert into client (id, email, password, name, surname, address_id, phone_number, is_deleted,
                        is_active, photo_id, role_id, loyalty_type, no_penalties, loyalty_points)
    values (nextval('my_seq_gen_user'), 'client1@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O',
                        'Marko', 'Markovic', 1, '0601231231', false, true, 1, 1, 0, 0, 124);

insert into cottage_owner (id, email, password, name, surname, address_id, phone_number, is_deleted,
                           is_active, photo_id, role_id, loyalty_type, loyalty_points)

values (nextval('my_seq_gen_user'), 'srdjan@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', 'Srdjan', 'Djuric', 1, '06222602323', false, true, 2,
        2, 0, 10);

insert into cottage (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                       rental_type, price, additional_services, no_rooms,cottage_owner_id)
    values (nextval('my_seq_gen_rental'), 'Vikendica Drvence', 'Lepa mala drvena vikendica na brdu.', 5, 'Nema lomljenja staklenih predmeta!',
                false, 8, 0, 0, 0, 90, 'wifi,parking,internet,bazen', 3, 5),
           (nextval('my_seq_gen_rental'), 'Fruskogorska zora', 'Najlepsa vikendica u okolini NS.', 5, 'Zabranjeno pusenje!',
            false, 9, 0, 0, 0, 100, 'wifi,klima,parking,rostilj', 4, 5),
           (nextval('my_seq_gen_rental'), 'Centar NS', 'Najlepsi pogled na centar NS.', 5, 'Zabranjeno pusenje!',
            false, 10, 0, 0, 0, 60, 'wifi,klima,parking,rostilj', 4, 5);

insert into loyalty_program (discount, increase, loyalty_type, price)
values (0, 0, 0, 0),
       (4, 4, 1, 100),
       (7, 7, 2, 200),
       (10, 10, 3, 300);

insert into fishing_instructor(id, email, is_active, is_deleted, loyalty_type, name, password, phone_number, surname,
                                role_id, address_id, photo_id, loyalty_points)
    values (nextval('my_seq_gen_user'), 'instructor@gmail.com', true, false, 0, 'Instro', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', '0613222126',
                'Instric', 4, 5, 1, 0),
           (nextval('my_seq_gen_user'), 'instructor2@gmail.com', false, false, 0, 'Instro2', 'sifra1223', '0619990009',
        'Instriiiic', 4, 3, 1, 0);

insert into adventure (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                       rental_type, price, biography, fishing_equipment, cancellation_conditions, instructor_id)
    values (nextval('my_seq_gen_rental'), 'Najjakija Avantura', 'Ide se na svakakva mesta, lepo skroz.', 10, 'Mora budete dobri, da se postujete i tako to.',
                false, 4, 0, 0, 2, 30, 'Skroz sam jak lik, ko avantura.', 'Pecaljka i tjt.', 10.5, 6),
            (nextval('my_seq_gen_rental'), 'Pecaj pecaj pecaj', 'Peca se ceo dan sta da kazem.', 15, 'Kako ja kazem tako se radi.',
                false, 5, 0, 0, 2, 25, 'Biografija, ne znam sta da napisem.', 'Sve je obezbedjeno.', 20.0, 6);

insert into request(id, is_answered, message, sent_time, request_type, sender_id)
    values (nextval('my_seq_gen_notification'), false, 'zeleo bih da mi se profil obrise iz razloga 111111111', '02/05/2022 23:44', 1, 6),
       (nextval('my_seq_gen_notification'), false, 'zeleo bih da mi se profil obrise iz razloga 222222222', '02/05/2022 13:14', 1, 7),
           (nextval('my_seq_gen_notification'), false, 'zelim da se registrujem jer mi se peca', '01/06/2022 13:14', 0, 7);

insert into review (id, is_answered, message, sent_time, grade, reciever_id, rental_service_id, sender_id)
    values (nextval('my_seq_gen_notification'), false, 'Svidelo mi se jako, ali klima ne radi.', '24/05/2022 20:33', 4.7, 2, 7, 4),
           (nextval('my_seq_gen_notification'), false, 'Svidelo mi se jakoooooooooo, ali klima ne radi.', '24/05/2022 02:33', 4.3, 2, 7, 4);


insert into subscription(owner_id, client_id, is_active_subscription)
    values (5, 4, true);

insert into reservation (start_time, end_time, is_action, is_reserved, price, rental_id, client_id, is_unavailable, action_services)
    values ('11/4/2022', '29/4/2022', false, true, 30, 7, 4, false, null),
            ('1/5/2022', '11/5/2022', false, true, 30, 8, 4, false, null),
            ('17/4/2022', '3/5/2022', false, true, 25, 2, 4, false, null),
            ('21/4/2022', '2/5/2022', false, true, 25, 2, 4, false, null),
            ('3/4/2022', '23/4/2022', false, true, 50, 3, 4, false, null),
            ('1/6/2022', '12/6/2022', false, true, 50, 3, 4, false, null),
            ('21/4/2022', '2/5/2022', false, true, 50, 3, 4, false, null),
            ('12/4/2022', '29/4/2022', false, true, 50, 4, 4, false, null),
            ('10/6/2022', '12/6/2022', true, false, 40, 4, null, false, 'klima rostilj'),
            ('15/6/2022', '27/6/2022', false, true, 30, 2, 4, false, null),
            ('18/6/2022', '21/6/2022', false, true, 40, 3, 4, false, null),
            ('21/6/2022', '25/6/2022', true, false, 60, 4, null, false, 'klima wifi rostilj');

insert into report(id, is_answered, message, sent_time, has_showed_up, is_negative, client_id, owner_id)
    values (nextval('my_seq_gen_notification'), false, 'Ovo nije bila najjakija avantura kao sto ste rekli', '05/06/2022 11:11', true, true , 4, 6),
           (nextval('my_seq_gen_notification'), false, 'Ovo nije bila najjakija avantura kao sto ste rekliiiiiiiiiiiii', '05/06/2022 22:22', true, true , 4, 6);

