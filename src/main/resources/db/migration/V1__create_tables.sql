drop table if exists captcha_codes;
drop table if exists global_settings;
drop table if exists post;
drop table if exists post_comments;
drop table if exists post_votes;
drop table if exists tag2post;
drop table if exists tags;
drop table if exists users;

create table captcha_codes (
id integer not null auto_increment,
 code TINYTEXT not null,
  secret_code TINYTEXT not null,
   time DATETIME not null,
    primary key (id));

create table global_settings (
id integer not null auto_increment,
 code varchar(255) not null,
  name varchar(255) not null,
   value varchar(255) not null,
    primary key (id));

create table post (
id integer not null auto_increment,
 is_active TINYINT(1) DEFAULT 1 not null,
  moderation_status VARCHAR(32) DEFAULT 'NEW' not null,
   moderator_id integer, text TEXT not null, time DATETIME not null,
    title varchar(255) not null,
     user_id integer not null,
      view_count integer not null,
       primary key (id));

create table post_comments (
id integer not null auto_increment,
 parent_id integer,
  post_id integer not null,
   text TEXT not null,
    time DATETIME not null,
     user_id integer not null,
      primary key (id));

create table post_votes (
id integer not null auto_increment,
 post_id integer not null,
  time DATETIME not null,
   user_id integer not null,
    value TINYINT(1) DEFAULT 0 not null,
     primary key (id));

create table tag2post (
id integer not null,
 post_id integer not null auto_increment,
  tag_id integer not null,
   primary key (post_id, tag_id));

create table tags (
id integer not null auto_increment,
 name varchar(255) not null,
  primary key (id));

create table users (
id integer not null auto_increment,
 code integer,
  email varchar(255) not null,
   is_moderator TINYINT not null,
    name varchar(255) not null,
     password integer not null,
      photo TEXT,
       reg_time DATETIME not null,
        primary key (id));

alter table post add constraint FK7ky67sgi7k0ayf22652f7763r foreign key (user_id) references users (id);
alter table post_comments add constraint FKmws3vvhl5o4t7r7sajiqpfe0b foreign key (post_id) references post (id);
alter table post_comments add constraint FKsnxoecngu89u3fh4wdrgf0f2g foreign key (user_id) references users (id);
alter table post_comments add constraint FKc3b7s6wypcsvua2ycn4o1lv2c foreign key (parent_id) references post_comments (id);
alter table post_votes add constraint FKkii0lkyj3a3jj95vgym33ho4b foreign key (post_id) references post (id);
alter table post_votes add constraint FK9q09ho9p8fmo6rcysnci8rocc foreign key (user_id) references users (id);
alter table tag2post add constraint FK2nnf4bm2w83lqajy78ib6eerb foreign key (post_id) references post (id);
alter table tag2post add constraint FKjou6suf2w810t2u3l96uasw3r foreign key (tag_id) references tags (id);