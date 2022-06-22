set datestyle to European;
INSERT INTO role (name) VALUES ('ROLE_CLIENT'),
                                ('ROLE_COTTAGE_OWNER'),
                                ('ROLE_BOAT_OWNER'),
                                ('ROLE_INSTRUCTOR'),
                                ('ROLE_ADMIN');


insert into address (country, place_name, street, lon, lat)
    values ('Srbija', 'Sabac', 'Karadjordjeva 26', '19.83383399956332', '45.25697997579121' ),
            ('Srbija', 'Novi Sad', 'Zeleznicka 36', '19.83383399956332', '45.25697997579121'),
            ('Srbija', 'Novi Sad', 'Sonje Marinkovic 5', '19.83383399956332', '45.25697997579121'),
            ('Srbija', 'Novi Sad', 'Bulevar Oslobodjenja 55', '19.83383399956332', '45.25697997579121'),
            ('Srbija', 'Novi Sad', 'Vase Pelagic 1', '19.840324439532647', '45.251395995531226'),
            ('Srbija', 'Sremska Kamenica', 'Karadjordjeva 33', '19.835541721737165', '45.22357844341107'),
            ('Srbija', 'Kula', 'Marsala Tita 200', '19.83383399956332', '45.25697997579121'),
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
           ('home-page-pic-boat.jpg', 3),
           ('home-page-pic-instructor.jpg', 1),
           ('home-page-pic-instructor.jpg', 2),
           ('home-page-pic-boat.jpg', 1),
           ('home-page-pic-boat.jpg', 2),
           ('boat1.jpg', 3),
           ('boat4.jpg', 3),
           ('boat2.jpg', 9),
           ('boat3.jpg', 10);

insert into admin (id, email, password, name, surname, address_id, phone_number, is_deleted, loyalty_points,
                        is_active, photo_id, role_id, loyalty_type)
    values (nextval('my_seq_gen_user'), 'admin1@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', 'Miroslav', 'Ilic', 2, '0614264444', false, 100,
                true, 2, 5, 0),
           (nextval('my_seq_gen_user'), 'admin2@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', 'Saban', 'Saulic', 3, '0614283764', false,100,
                true, 2, 5, 0);

insert into boat_owner (id, email, password, name, surname, address_id, phone_number, is_deleted,
                        is_active, photo_id, role_id, loyalty_type, loyalty_points)
    values (nextval('my_seq_gen_user'), 'boatowner1@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', 'Bojan', 'Bojanic', 7, '06222332323', false, true, 1,
        3, 0, 101);

insert into client (id, email, password, name, surname, address_id, phone_number, is_deleted, is_active,
                     photo_id, role_id, loyalty_type, loyalty_points, num_of_penalties)
     values (nextval('my_seq_gen_user'), 'client1@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O',
                         'Marko', 'Markovic', 1, '0601231231', false, true, 1, 1, 1, 224, 2);

insert into cottage_owner (id, email, password, name, surname, address_id, phone_number, is_deleted,
                           is_active, photo_id, role_id, loyalty_type, loyalty_points)
    values (nextval('my_seq_gen_user'), 'srdjan@gmail.com', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', 'Srdjan', 'Djuric', 1, '06222602323', false, true, 2,
        2, 0, 150);

insert into fishing_instructor(id, email, is_active, is_deleted, loyalty_type, name, password, phone_number, surname,
                                role_id, address_id, photo_id, loyalty_points)
    values (nextval('my_seq_gen_user'), 'instructor@gmail.com', true, false, 0, 'David', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', '0613222126',
                'Davidovic', 4, 5, 1, 250),
           (nextval('my_seq_gen_user'), 'instructor2@gmail.com', true, false, 0, 'Nikola', '$2a$10$34m5dosyTARXnOiqIEdM8uXyosZYQtDy75QBPPS7S91Iirn5ORQ8O', '0619990009',
        'Nikolic', 4, 3, 1, 0);

insert into adventure (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                            rental_type, price, biography, fishing_equipment, cancellation_conditions, instructor_id)
    values (nextval('my_seq_gen_rental'), 'Hod kroz kanjon', 'Ide se na svakakva mesta, lepo skroz.', 10, 'Grupa mora da prati vodica i da slusa sve njegove komande.',
                false, 4, 0, 0, 2, 30, '10 godina iskustva u radu.', 'torbe,odeca,satori', 10.5, 6),
            (nextval('my_seq_gen_rental'), 'Pecaj pecaj pecaj', 'Ucenje pecanja kroz igre.', 15, 'Nema skakanja u vodu sa brodica.',
                false, 5, 0, 0, 2, 25, '10 godina iskustva u radu.', 'mreza,stap', 20.0, 6);


insert into boat (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                      rental_type, price, type, boat_length, engine_number, engine_power, max_speed,
                      navigation_equipment, fishing_equipment, boat_owner_id)
    values (nextval('my_seq_gen_rental'), 'The Black Pearl', 'Luksuzna jahta. Najjaci brod za krstarenje!', 8, 'Nema skakanja sa broda.', false, 6, 4.5, 1, 1, 150,
                'Luksuzna jahta', '20', '46345754', '500', '30', 'radar,lidar,gps',
                'stap,mreza,dupla mreza', 3);

insert into cottage (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                       rental_type, price, additional_services, no_rooms,cottage_owner_id)
    values (nextval('my_seq_gen_rental'), 'Vikendica Drvence', 'Lepa mala drvena vikendica na brdu, dodjite!', 5, 'Nema lomljenja staklenih predmeta!',
                false, 8, 0, 0, 0, 90, 'wifi,parking,internet,bazen', 3, 5),
           (nextval('my_seq_gen_rental'), 'Fruskogorska zora', 'Najlepsa vikendica u okolini Novog Sada.', 5, 'Zabranjeno pusenje!',
            false, 9, 3.3, 2, 0, 100, 'wifi,klima,parking,rostilj', 4, 5),
           (nextval('my_seq_gen_rental'), 'Centar NS', 'Najlepsi pogled na centar NS. Pogled na katedralu.', 5, 'Zabranjeno pusenje!',
            false, 10, 0, 0, 0, 60, 'wifi,klima,parking,rostilj', 4, 5);

insert into loyalty_program (discount, increase, loyalty_type, price)
values (0, 0, 0, 0),
       (4, 4, 1, 100),
       (7, 7, 2, 200),
       (10, 10, 3, 300);


insert into adventure (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                       rental_type, price, biography, fishing_equipment, cancellation_conditions, instructor_id)
    values (nextval('my_seq_gen_rental'), 'Avantura ronjenja u Egejskom moru', 'Roni se u Egejskom moru.', 10, 'Ne smete se odvajati od instruktora.',
                false, 4, 0, 0, 2, 30, '10 godina iskustva u radu.', 'odelo za ronjenje,boca sa kiseonikom,peraja.', 10.5, 6),
            (nextval('my_seq_gen_rental'), 'Napredno pecanje', 'Pecanje velikih riba. Samo za one koji imaju bar 10 godina iskustva.', 15, 'Slusati instruktora.',
                false, 5, 0, 0, 2, 25, '10 godina iskustva u radu.', 'stap za pecanje,mreza', 20.0, 6);

insert into request(id, is_answered, message, sent_time, request_type, sender_id)
    values (nextval('my_seq_gen_notification'), false, 'Zeleo bih da mi se profil obrise iz razloga sto ne zelim da koristim aplikaciju.', '02/05/2022 23:44', 1, 4),
       (nextval('my_seq_gen_notification'), false, 'Zeleo bih da mi se profil obrise iz razloga sto ne koristim aplikaciju vise.', '02/05/2022 13:14', 1, 7),
           (nextval('my_seq_gen_notification'), false, 'Zelim da se registrujem jer mi se peca', '01/06/2022 13:14', 0, 7);

insert into boat (id, name, description, capacity, rules, is_deleted, address_id, average_rate, no_ratings,
                  rental_type, price, type, boat_length, engine_number, engine_power, max_speed,
                  navigation_equipment, fishing_equipment, boat_owner_id)
values (nextval('my_seq_gen_rental'), 'The White King', 'Ekstra brod, rezervisi odmah, luksuz na vodi!', 6, 'Nema lomljenja staklenih predmeta.', false, 6, 4.1, 1, 1, 70,
        'Ribarska jahta sa kabinom', '15', '46300754', '500', '20', 'radar,findfish,gps',
        'stap,mreza,mreza za ajkulu', 3),
       (nextval('my_seq_gen_rental'), 'Little boat', 'Mali brodic za manje rute! Odlican za pecanje!', 3, 'Nema skakanja sa brodica.', false, 6, 0, 0, 1, 30,
        'Ribarski brodic bez kabine', '5', '46350154', '200', '15', 'radar',
        'stap,mreza', 3);

insert into review (id, is_answered, message, sent_time, grade, receiver_id, rental_id, sender_id)
    values (nextval('my_seq_gen_notification'), false, 'Svidelo mi se jako, ali klima ne radi.', '24/05/2022 20:33', 4.7, 2, 7, 4),
           (nextval('my_seq_gen_notification'), false, 'Svidelo mi se jakoooooooooo, ali klima ne radi.', '24/05/2022 02:33', 4.3, 2, 7, 4),
           (nextval('my_seq_gen_notification'), true, 'Jako losa usluga, nisam zadovoljan.', '10/06/2022 20:33', 2.5, 5, 5, 4),
           (nextval('my_seq_gen_notification'), true, 'Prelepa priroda, divna druska gora!', '11/06/2022 17:45', 4.1, 5, 5, 4),
           (nextval('my_seq_gen_notification'), true, 'Odlican brod! Sjajno je bilo svee...', '24/05/2022 20:33', 4.5, 3, 3, 4),
           (nextval('my_seq_gen_notification'), true, 'Svidelo mi se jakoooooooooo, dobro pecanje.', '24/05/2022 02:33', 4.1, 3, 9, 4);


insert into subscription(owner_id, client_id, is_active_subscription)
    values (5, 4, true),
            (3, 4, true),
            (6, 4, true);

insert into report(id, is_answered, message, sent_time, has_showed_up, is_negative, client_id, owner_id, client_is_sender)
    values (nextval('my_seq_gen_notification'), false, 'Ne svidja mi se odnos instruktora prema grupi', '05/06/2022 11:11', true, true , 4, 6, true),
           (nextval('my_seq_gen_notification'), false, 'Sve je proslo kako treba', '05/06/2022 22:22', true, false , 4, 6, true);

insert into reservation (start_time, end_time, is_action, is_reserved, price, rental_id, client_id, is_unavailable, action_services, is_canceled)
values ('11/8/2022', '29/8/2022', false, true, 30, 1, 4, false, null, false),
       ('1/8/2022', '11/8/2022', false, true, 30, 1, 4, false, null, false),
       ('1/6/2022', '12/6/2022', false, true, 50, 3, 4, false, null, false),
       ('10/6/2022', '12/6/2022', false, true, 40, 4, 4, false, null, false),
       ('15/6/2022', '27/6/2022', false, true, 30, 2, 4, false, null, false),
       ('14/6/2022 11:00', '18/6/2022 11:00', false, true, 40, 6, 4, false, null, false),
       ('20/6/2022 11:00', '25/6/2022 11:00', false, true, 70, 5, 4, false, null, false),
       ('22/6/2022 11:00', '28/6/2022 11:00', true, false, 70, 4, 4, false, 'spa,table tennis', false),
       ('29/6/2022 11:00', '30/6/2022 11:00', true, true, 40, 4, 4, false, 'spa', false),
       ('14/6/2022 11:00', '15/6/2022 11:00', false, true, 150, 9, 4, false, null, false),
       ('23/6/2022 11:00', '25/6/2022 18:00', false, true, 50, 10, 4, false, null, false),
       ('10/7/2022 11:00', '14/7/2022 18:00', false, true, 50, 10, 4, false, null, false),
       ('18/6/2022', '21/6/2022', false, true, 40, 3, 4, false, null, false),
       ('21/4/2022', '22/4/2022', false, true, 60, 4, 4, false, null, false)

