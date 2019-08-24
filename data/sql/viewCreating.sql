CREATE VIEW projectDevelopers AS 
SELECT commits.author_id AS userID FROM commits
INNER JOIN projects ON
projects.id = commits.project_id
WHERE projects.forked_from IS null
UNION
SELECT user_id FROM pull_requests
INNER JOIN projects ON
projects.id = pull_requests.base_repo_id 
WHERE projects.forked_from IS null

CREATE VIEW baseProjects AS 
SELECT * FROM projects WHERE forked_from IS null AND language IS NOT null;
