select users.name, count(idUsers) as CountMessages
from (messages left join users on users.id = messages.idUsers)
where date = '2016-05-09'
group by name;