
-- DROP TABLE public.oauth_client_details;

CREATE TABLE public.oauth_client_details
(
  client_id character varying(255) NOT NULL,
  resource_ids character varying(255),
  client_secret character varying(255),
  scope character varying(255),
  authorized_grant_types character varying(255),
  web_server_redirect_uri character varying(255),
  authorities character varying(255),
  access_token_validity integer,
  refresh_token_validity bigint,
  additional_information character varying(4096),
  autoapprove character varying(4096),
  CONSTRAINT pk_oauth_client_details PRIMARY KEY (client_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_client_details
  OWNER TO postgres;

INSERT INTO oauth_client_details VALUES ('adminapp','mw/adminapp,ms/admin,ms/user','{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi','role_admin,role_superadmin','authorization_code,password,refresh_token,implicit',NULL,NULL,900,3600,'{}',NULL);


CREATE TABLE public.oauth_access_token
(
  token_id character varying(255),
  token bytea,
  authentication_id character varying(255) NOT NULL,
  user_name character varying(50),
  client_id character varying(255),
  authentication bytea,
  refresh_token character varying(255),
  CONSTRAINT pk_oauth_access_token PRIMARY KEY (authentication_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_access_token
  OWNER TO postgres;

-- Table: public.oauth_refresh_token

-- DROP TABLE public.oauth_refresh_token;

CREATE TABLE public.oauth_refresh_token
(
  token_id character varying(255),
  token bytea,
  authentication bytea
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.oauth_refresh_token
  OWNER TO postgres;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
CREATE TABLE permission
(
  id bigserial NOT NULL,
  name character varying(50) NOT NULL,
  created_by character varying(50) NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by character varying(50),
  last_modified_date timestamp without time zone,
  CONSTRAINT pk_permission PRIMARY KEY (id),
  CONSTRAINT permission_name_key UNIQUE (name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE permission
  OWNER TO postgres;


INSERT INTO permission VALUES (1,'can_delete_user','system','1970-01-01 00:00:00','1970-01-01 00:00:00',0),(2,'can_create_user','1970-01-01 00:00:00','1970-01-01 00:00:00',0),(3,'can_update_user','1970-01-01 00:00:00','1970-01-01 00:00:00',0),(4,'can_read_user','1970-01-01 00:00:00','1970-01-01 00:00:00',0);

--
-- Table structure for table `role`
--

CREATE TABLE role
(
  id bigserial NOT NULL,
  name character varying(50) NOT NULL,
  created_by character varying(50) NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by character varying(50),
  last_modified_date timestamp without time zone,
  CONSTRAINT pk_role PRIMARY KEY (id),
  CONSTRAINT role_name_key UNIQUE (name)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE role
  OWNER TO postgres;
INSERT INTO role VALUES (1,'role_admin','1970-01-01 00:00:00','1970-01-01 00:00:00',0),(2,'role_user','1970-01-01 00:00:00','1970-01-01 00:00:00',0);


--
-- Table structure for table `permission_role`
--

-- Table: common.role_permission_mapping

-- DROP TABLE permission_role;

CREATE TABLE permission_role
(
  permission_id bigint NOT NULL,
  role_id bigint NOT NULL,
  created_by character varying(50) NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by character varying(50),
  last_modified_date timestamp without time zone,
  version bigint NOT NULL DEFAULT '0',
  CONSTRAINT permission_role_pkey PRIMARY KEY (role_id, permission_id),
  CONSTRAINT permission_role_fk1 FOREIGN KEY (permission_id)
      REFERENCES permission (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT permission_role_fk2 FOREIGN KEY (role_id)
      REFERENCES role (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE permission_role
  OWNER TO postgres;


INSERT INTO permission_role VALUES (1,1,'1970-01-01 00:00:00','1970-01-01 00:00:00',0),(2,1,'1970-01-01 00:00:00','1970-01-01 00:00:00',0),(3,1,'1970-01-01 00:00:00','1970-01-01 00:00:00',0),(4,1,'1970-01-01 00:00:00','1970-01-01 00:00:00',0),(4,2,'1970-01-01 00:00:00','1970-01-01 00:00:00',0);

select * from permission

--
-- Table structure for table `user`
--



DROP TABLE "user";
CREATE TABLE "user" (
   id bigserial NOT NULL,
   username varchar(24) NOT NULL,
   password varchar(200) NOT NULL,
   email varchar(255) NOT NULL,
   enabled boolean NOT NULL,
   account_expired boolean NOT NULL,
   credentials_expired boolean NOT NULL,
   account_locked boolean NOT NULL,  
   created_by character varying(50) NOT NULL,
   created_date timestamp without time zone NOT NULL DEFAULT now(),
   last_modified_by character varying(50),
   last_modified_date timestamp without time zone,

   version bigint NOT NULL DEFAULT '0',
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT user_email_key UNIQUE (email),
  CONSTRAINT user_login_key UNIQUE (username)
) ;


INSERT INTO "user" VALUES (1,'admin','{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi','admin@example.com',TRUE,FALSE,FALSE,FALSE,'system','1970-01-01 00:00:00','system','1970-01-01 00:00:00',0),(2,'user','{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi','user@example.com',TRUE,FALSE,FALSE,FALSE,'system','1970-01-01 00:00:00','system','1970-01-01 00:00:00',0);


--
-- Table structure for table `role_user`
--

DROP TABLE IF EXISTS `role_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE role_user (
  role_id bigint  NOT NULL,
  user_id bigint  NOT NULL,
 created_by character varying(50) NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by character varying(50),
  last_modified_date timestamp without time zone,
  CONSTRAINT pk_role_user PRIMARY KEY (role_id,user_id),
  CONSTRAINT fk_role_user_user_id FOREIGN KEY (user_id)
      REFERENCES "user" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_role_user_role_id FOREIGN KEY (role_id)
      REFERENCES role (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) 


INSERT INTO role_user VALUES (1,1,'1970-01-01 00:00:00','1970-01-01 00:00:00',0),(2,2,'1970-01-01 00:00:00','1970-01-01 00:00:00',0);
