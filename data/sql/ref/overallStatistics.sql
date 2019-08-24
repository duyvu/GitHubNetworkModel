mysql -p -u duy

USE MSR14;

SHOW tables;

select count(*) from projects;
# There are 108291 projects
+----------+
| count(*) |
+----------+
|   108291 |
+----------+
1 row in set (0.00 sec)


select count(*) from projects where forked_from is null;
# It includes data from the top-10 starred software projects for the top programming languages on Github, which gives 90 projects and their forks
# For each project, we retrieved all data including issues, pull requests organizations, followers, stars and labels (milestones and events not included). 
# The dataset was constructed from scratch to ensure the latest information is in it.
+----------+
| count(*) |
+----------+
|       90 |
+----------+
1 row in set (0.00 sec)

select language,count(*) from projects where forked_from is null group by language;
#
+------------+----------+
| language   | count(*) |
+------------+----------+
| C          |       10 |
| C#         |        8 |
| C++        |        8 |
| CSS        |        3 |
| Go         |        1 |
| Java       |        8 |
| JavaScript |        9 |
| PHP        |        9 |
| Python     |       10 |
| R          |        4 |
| Ruby       |       10 |
| Scala      |        9 |
| TypeScript |        1 |
+------------+----------+
13 rows in set (0.00 sec)

select count(*) from projects where forked_from is not null;
# There are 108201 forked projects
+----------+
| count(*) |
+----------+
|   108201 |
+----------+
1 row in set (0.03 sec)

select count(*) from users;
# This is the total number of users who have any involment with 90 projects even it can be just a pull request.
+----------+
| count(*) |
+----------+
|   497476 |
+----------+
1 row in set (0.05 sec)

select id, login, name, location, created_at from users limit 10;
+----+----------------+-------------------------+-----------------+---------------------+
| id | login          | name                    | location        | created_at          |
+----+----------------+-------------------------+-----------------+---------------------+
|  1 | akka           | Akka Project            | Uppsala, Sweden | 2012-03-03 21:44:45 |
|  2 | hadley         | hadley wickham          | Houston, TX     | 2008-04-01 23:47:36 |
|  3 | johnmyleswhite | John Myles White        | Brooklyn, NY    | 2008-08-27 00:24:56 |
|  6 | mavam          | Matthias Vallentin      | Berkeley, CA    | 2009-02-12 10:12:03 |
|  7 | rstudio        | RStudio                 | Boston, MA      | 2010-12-08 05:03:35 |
|  8 | facebook       | Facebook                | Menlo Park, CA  | 2009-04-02 12:35:22 |
| 10 | yihui          | Yihui Xie               | Ames, IA        | 2009-12-07 15:42:04 |
| 12 | dockimbel      | Nenad Rakocevic         | Montenegro      | 2010-09-22 20:19:11 |
| 13 | mongodb        | mongodb                 | NULL            | 2009-01-09 03:02:04 |
| 14 | TTimo          | Timothee "TTimo" Besset | Dallas, TX, USA | 2008-04-11 22:54:48 |
+----+----------------+-------------------------+-----------------+---------------------+


select count(*) from followers;
# This is the total number of directed edges among observed users: follower_id -> user_id
+----------+
| count(*) |
+----------+
|  1469811 |
+----------+
1 row in set (0.12 sec)

select count(distinct id) from 
(select distinct follower_id as id from followers
union
select distinct user_id as id from followers) as network_users;
# These directed edges are among 353164 distinct users.
+--------------------+
| count(distinct id) |
+--------------------+
|             353164 |
+--------------------+
1 row in set (0.87 sec)

select count(distinct id) from
(select users.id as id from users
inner join followers
on users.id = followers.follower_id
union
select users.id as id from users
inner join followers
on users.id = followers.user_id) as networked_users;
# All users in the following network are observed in users
+--------------------+
| count(distinct id) |
+--------------------+
|             353164 |
+--------------------+
1 row in set (3.14 sec)

select count(distinct id) from
(select distinct id from 
(select distinct id from users
left join followers
on users.id = followers.follower_id
where followers.follower_id IS NULL) as leftJoin
left join followers
on leftJoin.id = followers.user_id
where followers.user_id IS NULL) as networked_users;
# There are 497476 - 353164 = 144312 observed users who do not follow others.
+--------------------+
| count(distinct id) |
+--------------------+
|             144312 |
+--------------------+
1 row in set (1.40 sec)

select count(distinct id) from commits;
# There are 555325 commit events
+--------------------+
| count(distinct id) |
+--------------------+
|             555325 |
+--------------------+
1 row in set (0.20 sec)

select count(distinct commits.id) from commits
inner join 
(select id from projects where forked_from is null) as topProjects
on topProjects.id = commits.project_id;
# There are 411278 out of 555325 commit events on the top projects. Other commit events could be on forked projects
+----------------------------+
| count(distinct commits.id) |
+----------------------------+
|                     411278 |
+----------------------------+
1 row in set (0.13 sec)

select count(distinct commits.id) from commits
inner join 
(select id from projects where forked_from is not null) as topProjects
on topProjects.id = commits.project_id;
# There are 385614 - 250287 = 135327 commit events on forked projects
+----------------------------+
| count(distinct commits.id) |
+----------------------------+
|                     135327 |
+----------------------------+

select count(distinct author_id) from commits;
# There are 23582 out of 497476 observed users are developers who made at least one commit event
+---------------------------+
| count(distinct author_id) |
+---------------------------+
|                     23582 |
+---------------------------+
1 row in set (0.04 sec)

select count(distinct commits.author_id) from commits
inner join 
(select id from projects where forked_from is null) as topProjects
on topProjects.id = commits.project_id;
# There are 15007 out of 23582 developers who work on the original top projects.
+-----------------------------------+
| count(distinct commits.author_id) |
+-----------------------------------+
|                             15007 |
+-----------------------------------+
1 row in set (0.22 sec)

select count(distinct commits.author_id) from commits
inner join 
(select id from projects where forked_from is not null) as topProjects
on topProjects.id = commits.project_id;
# There are 15870 out of 23582 developers who work on the forked projects.
+-----------------------------------+
| count(distinct commits.author_id) |
+-----------------------------------+
|                             15870 |
+-----------------------------------+
1 row in set (0.00 sec)

# There are 15007 + 15870 - 23582 = 7295 out of 23582 developers who work on both the original and forked projects.	
# There are 7,712 out of 23,582 developers who work exclusively on the original 90 projects. 
################################################################

select count(*) from projects;
# There are 108291 projects
+----------+
| count(*) |
+----------+
|   108291 |
+----------+

select count(*) from project_commits;
# There are 394765 project commit events
+----------+
| count(*) |
+----------+
|   394765 |
+----------+
1 row in set (0.08 sec)

select count(*) from project_commits
inner join (select id from projects where forked_from is null) as topProjects
on topProjects.id = project_commits.project_id;
# 251764 out of 394765 project commit events are from the original top projects.
+----------+
| count(*) |
+----------+
|   251764 |
+----------+
1 row in set (0.07 sec)

select count(*) from issue_events;
# There are 615757 issue_events;
+----------+
| count(*) |
+----------+
|   615757 |
+----------+
1 row in set (0.00 sec)

select count(distinct issue_id) from issue_events;
# from 110808 unique issues
+--------------------------+
| count(distinct issue_id) |
+--------------------------+
|                   110808 |
+--------------------------+
1 row in set (0.22 sec)

select id from projects where forked_from is null;

select * from projects where forked_from is null and id = 1;
+----+----------------------------------------+----------+------+--------------+----------+---------------------+--------------------------+-------------+---------+
| id | url                                    | owner_id | name | description  | language | created_at          | ext_ref_id               | forked_from | deleted |
+----+----------------------------------------+----------+------+--------------+----------+---------------------+--------------------------+-------------+---------+
|  1 | https://api.github.com/repos/akka/akka |        1 | akka | Akka Project | Scala    | 2009-02-16 22:51:54 | 52343e2ebd3543bb7f000002 |        NULL |       0 |
+----+----------------------------------------+----------+------+--------------+----------+---------------------+--------------------------+-------------+---------+

select count(*) from project_members where repo_id = 62501;

select count(*) from project_members where repo_id = 62501;

select distinct id from projects where forked_from is null;

select repo_id, count(user_id), name, language from project_members left join projects on projects.id = project_members.repo_id where projects.forked_from is null group by projects.id;
+---------+----------------+----------------------+------------+
| repo_id | count(user_id) | name                 | language   |
+---------+----------------+----------------------+------------+
|       1 |             11 | akka                 | Scala      |
|       2 |              4 | devtools             | R          |
|       3 |              4 | ProjectTemplate      | R          |
|       4 |              1 | stat-cookbook        | R          |
|       5 |              9 | hiphop-php           | C++        |
|       6 |              1 | knitr                | R          |
|       7 |             13 | shiny                | JavaScript |
|       8 |             17 | folly                | C++        |
|       9 |             39 | mongo                | C++        |
|      10 |              1 | doom3.gpl            | C++        |
|      11 |              4 | phantomjs            | C++        |
|      12 |             26 | TrinityCore          | C++        |
|     289 |              5 | MaNGOS               | C++        |
|     454 |              6 | bitcoin              | TypeScript |
|    3437 |              3 | mosh                 | C++        |
|    3583 |             70 | xbmc                 | C          |
|    3750 |              3 | http-parser          | C          |
|    5326 |              1 | beanstalkd           | C          |
|    7242 |              2 | redis                | C          |
|    7427 |              1 | ccv                  | C          |
|    8955 |              3 | memcached            | C          |
|    9215 |             16 | openFrameworks       | C          |
|    9636 |             30 | libgit2              | C          |
|   10380 |              4 | redcarpet            | C          |
|   10593 |              8 | libuv                | C          |
|   10629 |             10 | SignalR              | C#         |
|   12554 |              3 | SparkleShare         | C#         |
|   12976 |              3 | plupload             | JavaScript |
|   13566 |             66 | mono                 | C#         |
|   14327 |              2 | Nancy                | C#         |
|   14328 |              5 | ServiceStack         | C#         |
|   14912 |              2 | AutoMapper           | C#         |
|   15018 |              7 | RestSharp            | C#         |
|   15974 |              5 | ravendb              | C#         |
|   16134 |              4 | MiniProfiler         | Go         |
|   16402 |              8 | storm                | Java       |
|   17566 |              2 | ActionBarSherlock    | Java       |
|   19580 |             48 | facebook-android-sdk | Java       |
|   19786 |              5 | clojure              | Java       |
|   22980 |             13 | CraftBukkit          | Java       |
|   22981 |             15 | netty                | Java       |
|   23781 |             33 | android              | Java       |
|   24292 |             14 | node                 | JavaScript |
|   25875 |             12 | jquery               | JavaScript |
|   26388 |              8 | html5-boilerplate    | CSS        |
|   27504 |              1 | impress.js           | JavaScript |
|   42644 |              2 | d3                   | JavaScript |
|   47382 |             32 | chosen               | JavaScript |
|   49675 |              1 | Font-Awesome         | CSS        |
|   50618 |              1 | three.js             | JavaScript |
|   51669 |             28 | foundation           | JavaScript |
|   51671 |             13 | symfony              | PHP        |
|   59607 |             18 | CodeIgniter          | PHP        |
|   62501 |             40 | php-sdk              | PHP        |
|   62502 |             11 | zf2                  | PHP        |
|   63250 |             17 | cakephp              | PHP        |
|   64176 |              4 | ThinkUp              | PHP        |
|   64918 |              3 | phpunit              | PHP        |
|   65107 |              1 | Slim                 | PHP        |
|   69158 |              1 | django               | Python     |
|   71786 |              4 | tornado              | Python     |
|   71787 |              1 | httpie               | Python     |
|   72032 |              6 | flask                | Python     |
|   74914 |              5 | requests             | Python     |
|   75984 |             11 | reddit               | Python     |
|   76945 |              7 | boto                 | Python     |
|   77319 |              2 | Sick-Beard           | Python     |
|   78835 |              9 | django-cms           | Python     |
|   78852 |             36 | rails                | Ruby       |
|   79163 |              8 | homebrew             | Ruby       |
|   79166 |              5 | jekyll               | Ruby       |
|   91020 |             15 | gitlabhq             | Ruby       |
|   91331 |             25 | diaspora             | Ruby       |
|   95385 |             26 | devise               | Ruby       |
|  101472 |              5 | blueprint-css        | CSS        |
|  101997 |              3 | octopress            | Ruby       |
|  104306 |              1 | vinc.cc              | Ruby       |
|  104307 |             74 | paperclip            | Ruby       |
|  105378 |              7 | compass              | Ruby       |
|  106160 |              6 | finagle              | Scala      |
|  106161 |              5 | kestrel              | Scala      |
|  107085 |            313 | flockdb              | Scala      |
|  107186 |            313 | gizzard              | Scala      |
|  107534 |             12 | scala                | Scala      |
|  107535 |              9 | scalatra             | Scala      |
|  107672 |            313 | zipkin               | Scala      |
+---------+----------------+----------------------+------------+

select count(*) from commits
left join projects on projects.id = commits.project_id where projects.forked_from is null;
# Commit events to top projects
+----------+
| count(*) |
+----------+
|   411278 |
+----------+

select count(*) from commits
left join (select repo_id, user_id from project_members left join projects on projects.id = project_members.repo_id where projects.forked_from is null) as topProjectTeamMembers
on topProjectTeamMembers.repo_id = commits.project_id and topProjectTeamMembers.user_id = author_id;

select count(*) from
(select commits.author_id, commits.id from commits
left join projects on projects.id = commits.project_id where projects.forked_from is null) as filteredProjectCommits
left join (select user_id from project_members left join projects on projects.id = project_members.repo_id where projects.forked_from is null) as filteredTeamMembers
on filteredProjectCommits.author_id = filteredTeamMembers.user_id limit 10;

select count(*) from 
(select commits.project_id, commits.id from commits
inner join (select user_id from project_members left join projects on projects.id = project_members.repo_id where projects.forked_from is null) as filteredTeamMembers
on filteredTeamMembers.user_id = commits.author_id) as filteredMemberCommits
inner join (select id from projects where forked_from is null) as TopProjects
on filteredMemberCommits.project_id = TopProjects.id;
# Commit events from core team members to top projects
+----------+
| count(*) |
+----------+
|   258524 |
+----------+

select count(*) from 
(select pull_requests.base_repo_id, pull_requests.id from pull_requests
inner join (select user_id from project_members left join projects on projects.id = project_members.repo_id where projects.forked_from is null) as filteredTeamMembers
on filteredTeamMembers.user_id = pull_requests.user_id) as filteredContributorRequests
inner join (select id from projects where forked_from is null) as TopProjects
on filteredContributorRequests.base_repo_id = TopProjects.id;
# Pull requests from core team members to top projects
+----------+
| count(*) |
+----------+
|    23068 |
+----------+

select count(*) from pull_requests
inner join projects on
projects.id = pull_requests.base_repo_id 
where projects.forked_from is null
# Pull requests to top projects
+----------+
| count(*) |
+----------+
|    78955 |
+----------+

select count(distinct commits.author_id) from commits
inner join 
(select id from projects where forked_from is null) as topProjects
on topProjects.id = commits.project_id;


select user_id from 
(select commits.author_id as user_id from commits
inner join projects on
projects.id = commits.project_id
where projects.forked_from is null
union
select user_id from pull_requests
inner join projects on
projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as projectDevelopers;

select count(*) from followers
inner join (select commits.author_id as user_id from commits
inner join projects on
projects.id = commits.project_id
where projects.forked_from is null
union
select user_id from pull_requests
inner join projects on
projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as projectDevelopers1
on projectDevelopers1.user_id = followers.user_id

select count(*) from 
(select followers.user_id, followers.follower_id from followers
inner join (select commits.author_id as user_id from commits
inner join projects on
projects.id = commits.project_id
where projects.forked_from is null
union
select user_id from pull_requests
inner join projects on
projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as projectDevelopers1
on projectDevelopers1.user_id = followers.user_id) as edges1
inner join (select commits.author_id as user_id from commits
inner join projects on
projects.id = commits.project_id
where projects.forked_from is null
union
select user_id from pull_requests
inner join projects on
projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as projectDevelopers2
on projectDevelopers2.user_id = edges1.follower_id



select count(distinct id) from 
(select distinct follower_id as id from followers
union
select distinct user_id as id from followers) as network_users;

select language,id,name,created_at from projects where forked_from is null order by created_at;
