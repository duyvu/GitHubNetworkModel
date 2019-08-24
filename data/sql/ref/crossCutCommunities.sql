# Copy file to default data directory
# sudo cp /var/lib/mysql/MSR14/fileName.txt destination

select commits.author_id, commits.project_id, count(commits.id) as eventCount
from projects  
left join commits on projects.id = commits.project_id
where projects.forked_from is null and projects.language is not null
group by commits.author_id, commits.project_id 
order by eventCount
into OUTFILE 'developerProjectCommits.txt'
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n';
