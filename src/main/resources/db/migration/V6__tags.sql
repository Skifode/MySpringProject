INSERT INTO `tags` (`id`, `name`) VALUES ('1', 'Java');
INSERT INTO `tags` (`id`, `name`) VALUES ('2', 'php');
INSERT INTO `tags` (`id`, `name`) VALUES ('3', 'разработка');
INSERT INTO `tags` (`id`, `name`) VALUES ('4', 'разное');
INSERT INTO `mydiplomdb`.`tags` (`id`, `name`) VALUES ('5', 'дизайн');
INSERT INTO `mydiplomdb`.`tags` (`id`, `name`) VALUES ('6', 'JavaScript');

INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('1', '1', '1');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('2', '2', '1');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('3', '2', '3');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('4', '3', '1');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('5', '3', '3');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('6', '4', '4');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('7', '5', '1');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('8', '6', '2');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('9', '6', '3');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('10', '7', '2');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('11', '6', '3');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('12', '8', '2');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('13', '9', '2');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('14', '10', '5');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('15', '11', '6');
INSERT INTO `tag2post` (`id`, `post_id`, `tag_id`) VALUES ('16', '12', '6');

