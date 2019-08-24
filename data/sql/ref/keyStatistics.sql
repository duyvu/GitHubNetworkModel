# Copy file to default data directory
# sudo cp /var/lib/mysql/MSR14/fileName.txt destination

SHOW tables;

select count(*) from projects where forked_from is null;
# It includes data from the top-10 starred software projects for the top programming languages on Github, which gives 90 projects and their forks
# For each project, we retrieved all data including issues, pull requests organizations, followers, stars and labels (milestones and events not included). 
# The dataset was constructed from scratch to ensure the latest information is in it.
# There are 90 base projects (1 is dump one, see the next query results)
+----------+
| count(*) |
+----------+
|       91 |
+----------+
1 row in set (0.00 sec)

select * from projects where forked_from is null and language is null;
# There is one dump project
+--------+-------------------------------------------------+----------+--------+-------------+----------+---------------------+--------------------------+-------------+---------+
| id     | url                                             | owner_id | name   | description | language | created_at          | ext_ref_id               | forked_from | deleted |
+--------+-------------------------------------------------+----------+--------+-------------+----------+---------------------+--------------------------+-------------+---------+
| 108342 | https://api.github.com/repos/Craftbukkit/Bukkit |   505581 | Bukkit |             | NULL     | 2011-05-31 00:05:27 | 524d8253bd3543afc8000002 |        NULL |       0 |
+--------+-------------------------------------------------+----------+--------+-------------+----------+---------------------+--------------------------+-------------+---------+
1 row in set (0.00 sec)

select * from commits where project_id = 108342;
Empty set (0.00 sec)

select * from project_members where repo_id = 108342;
Empty set (0.00 sec)

select language,count(*) from projects where forked_from is null and language is not null group by language;
# Base projects by programming languages
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
13 rows in set (0.01 sec)

select count(*) from commits
inner join projects 
on projects.id = commits.project_id 
where projects.forked_from is null and projects.language is not null;
# Commit events to base projects
+----------+
| count(*) |
+----------+
|   411278 |
+----------+
1 row in set (0.06 sec)

select count(*) from pull_requests
inner join projects 
on projects.id = pull_requests.base_repo_id 
where projects.forked_from is null
# Pull requests to base projects
+----------+
| count(*) |
+----------+
|    78955 |
+----------+
1 row in set (0.02 sec)

select count(*) from 
(select commits.author_id as user_id from commits
inner join projects on
projects.id = commits.project_id
where projects.forked_from is null
union
select user_id from pull_requests
inner join projects on
projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as projectDevelopers;
# Project developers are those who have made at least one commit or pull-request event.
+----------+
| count(*) |
+----------+
|    25866 |
+----------+
1 row in set (0.47 sec)

select count(distinct commits.author_id)
from projects  
left join project_members on projects.id = project_members.repo_id
left join commits on projects.id = commits.project_id
where projects.forked_from is null and projects.language is not null;
# Project developers are those who have made at least one commit event.
+-----------------------------------+
| count(distinct commits.author_id) |
+-----------------------------------+
|                             17435 |
+-----------------------------------+

select projects.id, name, projects.created_at, language, 
count(distinct project_members.user_id), count(distinct commits.id), 
count(distinct commits.author_id)
from projects  
left join project_members on projects.id = project_members.repo_id
left join commits on projects.id = commits.project_id
where projects.forked_from is null and projects.language is not null
group by projects.id order by projects.created_at, name
into OUTFILE 'projectsSummaryCommitOnly.txt'
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n';
# sudo cp /var/lib/mysql/MSR14/projectsSummaryCommitOnly.txt workspace/arcnets-notes/gitHub90/tables
# > data = read.table("projectsSummaryCommitOnly.txt", sep=",")
# > sum(data$V7)
#[1] 19430 > 17435
# There are some developers who have contributed (i.e. code committing) to at least 2 projects

select projects.id, name, projects.created_at, language, 
count(distinct project_members.user_id), count(distinct commits.id), 
count(distinct commits.author_id), count(distinct pull_requests.user_id)
from projects  
left join project_members on projects.id = project_members.repo_id
left join commits on projects.id = commits.project_id
left join pull_requests on projects.id = pull_requests.base_repo_id 
where projects.forked_from is null and projects.language is not null
group by projects.id order by projects.created_at, name
into OUTFILE 'projectsSummaryCommitPullRequest.txt';

select count(*) from followers
inner join (select commits.author_id as user_id from commits
inner join projects on
projects.id = commits.project_id
where projects.forked_from is null
union
select user_id from pull_requests
inner join projects on
projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as contributors1
on followers.follower_id = contributors1.user_id
inner join (select commits.author_id as user_id from commits
inner join projects on
projects.id = commits.project_id
where projects.forked_from is null
union
select user_id from pull_requests
inner join projects on
projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as contributors2
on followers.user_id = contributors2.user_id;
# The following edges among project developers
+----------+
| count(*) |
+----------+
|    82994 |
+----------+
1 row in set (3 min 0.40 sec)

select commit_id, count(distinct user_id) as commenters from commit_comments
group by commit_id order by commenters DESC
limit 10;
+-----------+------------+
| commit_id | commenters |
+-----------+------------+
|    524509 |        193 |
|    516027 |        132 |
|    124678 |        123 |
|    300183 |         90 |
|    543720 |         84 |
|    209486 |         71 |
|    151540 |         68 |
|    536004 |         59 |
|    311757 |         40 |
|    540694 |         40 |
+-----------+------------+
10 rows in set (0.06 sec)

select count(*) from commit_comments
inner join 
(select commits.id as commit_id from commits
inner join projects 
on projects.id = commits.project_id 
where projects.forked_from is null) as projectCommits
on commit_comments.commit_id = projectCommits.commit_id;
# Commit comments on the projects
+----------+
| count(*) |
+----------+
|    48282 |
+----------+
1 row in set (0.46 sec)

select issue_id, count(distinct user_id) as commenters from issue_comments
group by issue_id order by commenters DESC
limit 10;
+----------+------------+
| issue_id | commenters |
+----------+------------+
|    42772 |        189 |
|    29970 |        160 |
|   145412 |        137 |
|    40294 |         78 |
|    38552 |         61 |
|    37992 |         60 |
|   114353 |         60 |
|   142442 |         57 |
|   136608 |         56 |
|   128497 |         55 |
+----------+------------+
10 rows in set (0.81 sec)

select count(*) from pull_request_comments
inner join
(select pull_requests.id as pull_request_id from pull_requests
inner join projects 
on projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as projectPullRequests
on pull_request_comments.pull_request_id = projectPullRequests.pull_request_id;
# Pull-request comments on the base projects
+----------+
| count(*) |
+----------+
|    54892 |
+----------+
1 row in set (0.11 sec)

select pull_request_comments.pull_request_id, count(distinct user_id) as commenters from pull_request_comments
inner join
(select pull_requests.id as pull_request_id from pull_requests
inner join projects 
on projects.id = pull_requests.base_repo_id 
where projects.forked_from is null) as projectPullRequests
on pull_request_comments.pull_request_id = projectPullRequests.pull_request_id
group by pull_request_comments.pull_request_id
order by commenters DESC
limit 10;
# Commenters on pull_request_comments
+-----------------+------------+
| pull_request_id | commenters |
+-----------------+------------+
|           36565 |         11 |
|           24393 |         10 |
|           26658 |          9 |
|           78273 |          9 |
|           26010 |          9 |
|           81877 |          9 |
|           30801 |          9 |
|           33152 |          9 |
|           30904 |          9 |
|           44536 |          8 |
+-----------------+------------+
10 rows in set (0.14 sec)


select count(*) from issues
inner join projects 
on projects.id = issues.repo_id 
where projects.forked_from is null;
# The number of issues on the base projects
+----------+
| count(*) |
+----------+
|   150362 |
+----------+
1 row in set (0.03 sec)

select count(*) from issue_comments
inner join 
(select issues.id as issue_id from issues
inner join projects 
on projects.id = issues.repo_id 
where projects.forked_from is null) as projectIssues
on issue_comments.issue_id = projectIssues.issue_id;
# Issue comments on the base projects
+----------+
| count(*) |
+----------+
|   534104 |
+----------+
1 row in set (0.00 sec)

select count(*) from projects
inner join
where projects.forked_from is null

select repo_id, count(user_id) as devCounts from project_members group by repo_id having count(user_id) > 1 order by devCounts desc;

select created_at from projects where forked_from is null and language is not null;
