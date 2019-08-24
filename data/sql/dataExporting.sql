mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT created_at AS time, userID FROM projectDevelopers 
INNER JOIN users ON 
users.id = projectDevelopers.userID 
ORDER BY time" >> events/users.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT created_at AS time, follower_id AS senderID, user_id AS receiverID from followers
INNER JOIN projectDevelopers AS  projectDevelopers1
ON projectDevelopers1.userID = followers.user_id
INNER JOIN projectDevelopers AS projectDevelopers2
ON projectDevelopers2.userID = followers.follower_id
ORDER BY time" >> events/followers.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT created_at AS time, id AS repoID, owner_id AS ownerID, name, language FROM baseProjects 
ORDER BY time" >> events/projects.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT project_members.created_at AS time, project_members.user_id AS userID, project_members.repo_id AS repoID FROM project_members 
INNER JOIN baseProjects ON 
project_members.repo_id = baseProjects.id 
ORDER BY time" >> events/projectMembers.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT issues.created_at AS time, issues.id AS issueID, repo_id AS repoID, COALESCE(pull_request_id, -1) as pullRequestID FROM issues 
INNER JOIN baseProjects ON 
issues.repo_id = baseProjects.id 
ORDER BY time" >> events/issues.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT issue_events.created_at AS time, issue_events.issue_id AS issueID, issue_events.actor_id AS userID FROM issue_events
INNER JOIN issues ON
issues.id = issue_events.issue_id
INNER JOIN baseProjects ON 
issues.repo_id = baseProjects.id
WHERE issue_events.action = 'closed'
ORDER BY time" >> events/closedIssues.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT issue_comments.created_at AS time, issue_comments.comment_id as commentID, issue_comments.user_id AS userID, issue_comments.issue_id AS issueID, baseProjects.id as repoID FROM issue_comments 
INNER JOIN projectDevelopers ON 
issue_comments.user_id = projectDevelopers.userID
INNER JOIN issues ON
issue_comments.issue_id = issues.id
INNER JOIN baseProjects ON
issues.repo_id = baseProjects.id
ORDER BY time" >> events/issueComments.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT commits.created_at AS time, commits.id as commitID, author_id as authorID, committer_id AS committerID, project_id as repoID FROM commits 
INNER JOIN baseProjects ON 
commits.project_id = baseProjects.id 
ORDER BY time" >> events/commits.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT commit_comments.created_at AS time, commit_comments.id AS commentID, user_id AS userID, commit_comments.commit_id AS commitID, COALESCE(line, -1) AS line, COALESCE(position, -1) AS position FROM commit_comments
INNER JOIN commits ON
commit_comments.commit_id = commits.id
INNER JOIN baseProjects ON 
commits.project_id = baseProjects.id 
ORDER BY time" >> events/commitComments.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT pull_request_history.created_at AS time, pull_requests.id AS pullRequestID, user_id AS userID, base_repo_id AS repoID FROM pull_requests 
INNER JOIN pull_request_history ON
pull_requests.id = pull_request_history.pull_request_id
INNER JOIN baseProjects ON
pull_requests.base_repo_id = baseProjects.id
INNER JOIN projectDevelopers ON
pull_requests.user_id = projectDevelopers.userID 
WHERE pull_request_history.action = 'opened'
ORDER BY time" >> events/pullRequests.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT pull_request_history.created_at AS time, pull_requests.id AS pullRequestID, user_id AS userID, base_repo_id AS repoID FROM pull_requests 
INNER JOIN pull_request_history ON
pull_requests.id = pull_request_history.pull_request_id
INNER JOIN baseProjects ON
pull_requests.base_repo_id = baseProjects.id
INNER JOIN projectDevelopers ON
pull_requests.user_id = projectDevelopers.userID 
WHERE pull_request_history.action = 'closed'
ORDER BY time" >> events/closedPullRequests.csv

mysql --host=localhost --user=root --password=abcd1234 MSR14 -e "SELECT pull_request_comments.created_at AS time, comment_id AS commentID, pull_request_comments.user_id AS userID, pull_request_id AS pullRequestID, commit_id AS commitID FROM pull_request_comments
INNER JOIN pull_requests ON
pull_requests.id = pull_request_comments.pull_request_id
INNER JOIN baseProjects ON
pull_requests.base_repo_id = baseProjects.id
INNER JOIN projectDevelopers ON
pull_request_comments.user_id = projectDevelopers.userID 
ORDER BY time" >> events/pullRequestComments.csv

