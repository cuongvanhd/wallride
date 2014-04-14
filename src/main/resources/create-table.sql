create table article (
  id bigint not null,
  primary key (id)
) ENGINE=InnoDB;

create table article_category (
  category_id bigint not null,
  article_id bigint not null,
  primary key (article_id, category_id)
) ENGINE=InnoDB;

create table article_link (
  id bigint not null auto_increment,
  title longtext not null,
  url longtext not null,
  language varchar(3) not null,
  article_id bigint,
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  primary key (id)
) ENGINE=InnoDB;

create table article_tag (
  article_id bigint not null,
  tag_id bigint not null,
  primary key (article_id, tag_id)
) ENGINE=InnoDB;

create table category (
  id bigint not null auto_increment,
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  code varchar(200) not null,
  description longtext,
  language varchar(3) not null,
  lft integer not null,
  name varchar(200) not null,
  rgt integer not null,
  parent_id bigint,
  primary key (id)
) ENGINE=InnoDB;

create table media (
  id varchar(50) not null,
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  mime_type varchar(50) not null,
  original_name varchar(500),
  primary key (id)
) ENGINE=InnoDB;

create table movement (
  td11_id integer not null,
  td11_clubid integer,
  td11_leageid integer,
  td11_playerid integer,
  td11_participation1 integer,
  td11_participation1_2 integer,
  td11_score2 integer,
  td11_participation2_2 integer,
  td11_participation3_2 integer,
  td11_bikou longtext,
  td11_score integer,
  td11_participation2 integer,
  td11_participation3 integer,
  td11_uniformnumber varchar(255),
  s varchar(255),
  s2 varchar(255),
  seson2 varchar(255),
  seson1 varchar(255),
  primary key (td11_id)
) ENGINE=InnoDB;

create table navigation_item (
  id bigint not null auto_increment,
  type varchar(31) not null,
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  language varchar(255) not null,
  sort integer not null,
  parent_id bigint,
  category_id bigint,
  page_id bigint,
  primary key (id)
) ENGINE=InnoDB;

create table page (
  id bigint not null,
  lft integer not null,
  rgt integer not null,
  parent_id bigint,
  primary key (id)
) ENGINE=InnoDB;

create table player_news (
  id integer not null,
  permalink longtext,
  published datetime,
  source varchar(255),
  title varchar(255),
  primary key (id)
) ENGINE=InnoDB;

create table post (
  id bigint not null auto_increment,
  date datetime,
  code varchar(200),
  title varchar(200),
  body longtext,
  language varchar(3) not null,
  status varchar(50) not null,
  seo_title varchar(1000),
  seo_description longtext,
  seo_keywords longtext,
  og_app_id varchar(255),
  og_description longtext,
  og_image longtext,
  og_locale varchar(255),
  og_sitename longtext,
  og_title longtext,
  og_type varchar(255),
  og_url longtext,
  author_id bigint,
  cover_id varchar(50),
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  primary key (id)
) ENGINE=InnoDB;

create table post_media (
  post_id bigint not null,
  media_id varchar(50) not null,
  `index` integer not null,
  primary key (post_id, `index`)
) ENGINE=InnoDB;

create table setting (
  `key` varchar(100) not null,
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  value varchar(500) not null,
  primary key (`key`)
) ENGINE=InnoDB;

create table tag (
  id bigint not null auto_increment,
  name varchar(500) not null,
  language varchar(3) not null,
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  primary key (id)
) ENGINE=InnoDB;

create table td13_leageclub (
  td13_id integer not null,
  td13_clubid integer,
  td13_leageid integer,
  primary key (td13_id)
) ENGINE=InnoDB;

create table tm02_club (
  tm02_id integer not null,
  tm02_alias varchar(255),
  tm02_alias_jp varchar(255),
  tm02_baseground varchar(255),
  tm02_chairmanname varchar(255),
  tm02_changememo varchar(255),
  tm02_countrycd integer,
  tm02_establishment varchar(255),
  tm02_etc varchar(255),
  tm02_realname varchar(255),
  tm02_real3tr varchar(255),
  tm02_realname_jp varchar(255),
  tm02_petname varchar(255),
  tm02_websiteurl varchar(255),
  tm02_years integer,
  primary key (tm02_id)
) ENGINE=InnoDB;

create table tm05_player (
  tm05_id integer not null,
  tm05_birthday varchar(255),
  tm05_comment longtext,
  tm05_tall float,
  tm05_home varchar(255),
  tm05_seekword varchar(255),
  tm05_movie longtext,
  tm05_fullname varchar(255),
  tm05_name varchar(255),
  tm05_fullname_jp varchar(255),
  tm05_position_division varchar(255),
  tm05_wait float,
  tm05_workingfoot integer,
  tm05_nationality1 integer,
  primary key (tm05_id)
) ENGINE=InnoDB;

create table tm06_world (
  tm06_id integer not null,
  tm06_fifa_abbreviation varchar(255),
  tm06_name varchar(255),
  tm06_englishname varchar(255),
  tm06_realname varchar(255),
  tm06_realname_jp varchar(255),
  primary key (tm06_id)
) ENGINE=InnoDB;

create table tm07_leage (
  tm07_id integer not null,
  tm07_countryid_sub varchar(255),
  tm07_division integer,
  tm07_leageend varchar(255) not null,
  tm07_foreigner varchar(255),
  tm07_image1 varchar(255),
  tm07_move varchar(255),
  tm07_leagename varchar(255),
  tm07_leagename2 varchar(255),
  tm07_leagestart varchar(255) not null,
  tm07_updateymd datetime,
  tm07_warning varchar(255),
  tm07_countryid integer,
  primary key (tm07_id)
) ENGINE=InnoDB;

create table tm14_leage_sub (
  tm14_id integer not null,
  td14_leageid integer,
  primary key (tm14_id)
) ENGINE=InnoDB;

create table user (
  id bigint not null auto_increment,
  email varchar(500) not null,
  login_id varchar(100) not null,
  login_password varchar(500) not null,
  name_first varchar(50) not null,
  name_last varchar(50) not null,
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  primary key (id)
) ENGINE=InnoDB;

create table user_invitation (
  token varchar(50) not null,
  email varchar(500) not null,
  message longtext,
  expired_at datetime not null,
  accepted boolean not null,
  accepted_at datetime,
  created_at datetime not null,
  created_by varchar(100),
  updated_at datetime not null,
  updated_by varchar(100),
  primary key (token)
) ENGINE=InnoDB;

alter table article
add index FK_3mlcrjv9clnvarg3o8fysvnkx (id),
add constraint FK_3mlcrjv9clnvarg3o8fysvnkx
foreign key (id)
references post (id);

alter table article_category
add index FK_48nv4hxwx3mte5gferj9wlj46 (article_id),
add constraint FK_48nv4hxwx3mte5gferj9wlj46
foreign key (article_id)
references article (id);

alter table article_category
add index FK_aaq7a2c3e34qghxyh34ao8r6p (category_id),
add constraint FK_aaq7a2c3e34qghxyh34ao8r6p
foreign key (category_id)
references category (id);

alter table article_link
add index FK_clklroeqrvgtqm2wgdkedvayx (article_id),
add constraint FK_clklroeqrvgtqm2wgdkedvayx
foreign key (article_id)
references article (id);

alter table article_tag
add index FK_pkndl0ud6fkak73gdkls858a5 (tag_id),
add constraint FK_pkndl0ud6fkak73gdkls858a5
foreign key (tag_id)
references tag (id);

alter table article_tag
add index FK_5ao70rbptu4cd93wbu7o38y1y (article_id),
add constraint FK_5ao70rbptu4cd93wbu7o38y1y
foreign key (article_id)
references article (id);

alter table category
add constraint UK_86l62kycx6uuh2dbgymn8i065 unique (code, language);

alter table category
add index FK_81thrbnb8c08gua7tvqj7xdqk (parent_id),
add constraint FK_81thrbnb8c08gua7tvqj7xdqk
foreign key (parent_id)
references category (id);

alter table movement
add index FK_pxhq6yvjpm2kutp16kvmh5teq (td11_clubid),
add constraint FK_pxhq6yvjpm2kutp16kvmh5teq
foreign key (td11_clubid)
references tm02_club (tm02_id);

alter table movement
add index FK_lvm1h9ehj7vw0mwwc622g82cu (td11_leageid),
add constraint FK_lvm1h9ehj7vw0mwwc622g82cu
foreign key (td11_leageid)
references tm07_leage (tm07_id);

alter table movement
add index FK_1ryk64i97ten5q6qd6889a2i1 (td11_playerid),
add constraint FK_1ryk64i97ten5q6qd6889a2i1
foreign key (td11_playerid)
references tm05_player (tm05_id);

alter table navigation_item
add index FK_e986fb2rhw2a7a2m2col2f1fg (parent_id),
add constraint FK_e986fb2rhw2a7a2m2col2f1fg
foreign key (parent_id)
references navigation_item (id);

alter table navigation_item
add index FK_qie2cbixacp4xccixia3mjd99 (category_id),
add constraint FK_qie2cbixacp4xccixia3mjd99
foreign key (category_id)
references category (id);

alter table navigation_item
add index FK_gqekdbas3sbmx3u4peurmkxl0 (page_id),
add constraint FK_gqekdbas3sbmx3u4peurmkxl0
foreign key (page_id)
references page (id);

alter table page
add index FK_e9x1tbh3hitjnkmigkv8pl24w (parent_id),
add constraint FK_e9x1tbh3hitjnkmigkv8pl24w
foreign key (parent_id)
references page (id);

alter table page
add index FK_88lc5ox4n3kvd7vc10nvx8nn6 (id),
add constraint FK_88lc5ox4n3kvd7vc10nvx8nn6
foreign key (id)
references post (id);

alter table post
add constraint UK_6khu2naokwmhyfq3lt8t8eehn unique (code, language);

alter table post
add index FK_ik65bluepv8oxdfvgbj5qdcsj (author_id),
add constraint FK_ik65bluepv8oxdfvgbj5qdcsj
foreign key (author_id)
references user (id);

alter table post
add index FK_lew3sxka65cx9ichkheda3m4p (cover_id),
add constraint FK_lew3sxka65cx9ichkheda3m4p
foreign key (cover_id)
references media (id);

alter table post_media
add index FK_cbh3kwx9ocobb3y3jn93nth0o (media_id),
add constraint FK_cbh3kwx9ocobb3y3jn93nth0o
foreign key (media_id)
references media (id);

alter table post_media
add index FK_rmb5w9waqw5fpy31j42wjirt3 (post_id),
add constraint FK_rmb5w9waqw5fpy31j42wjirt3
foreign key (post_id)
references post (id);

alter table td13_leageclub
add index FK_qtmlc60r6i4u748rir29bvggv (td13_clubid),
add constraint FK_qtmlc60r6i4u748rir29bvggv
foreign key (td13_clubid)
references tm02_club (tm02_id);

alter table td13_leageclub
add index FK_7voibt4v0ybt3vda952mrb1l6 (td13_leageid),
add constraint FK_7voibt4v0ybt3vda952mrb1l6
foreign key (td13_leageid)
references tm07_leage (tm07_id);

alter table tm05_player
add index FK_1cu1l3l58j0r64cwv1gtbjiv9 (tm05_nationality1),
add constraint FK_1cu1l3l58j0r64cwv1gtbjiv9
foreign key (tm05_nationality1)
references tm06_world (tm06_id);

alter table tm07_leage
add index FK_stv9nuq8hm7ajngv1o2dtxsiv (tm07_countryid),
add constraint FK_stv9nuq8hm7ajngv1o2dtxsiv
foreign key (tm07_countryid)
references tm06_world (tm06_id);

alter table tm14_leage_sub
add index FK_93q4opvyf7ld3l4gsfdxa0qy7 (td14_leageid),
add constraint FK_93q4opvyf7ld3l4gsfdxa0qy7
foreign key (td14_leageid)
references tm07_leage (tm07_id);

alter table user
add constraint UK_6ntlp6n5ltjg6hhxl66jj5u0l unique (login_id);

create table persistent_logins (
	username varchar(64) not null,
	series varchar(64) primary key,
	token varchar(64) not null,
	last_used timestamp not null
);