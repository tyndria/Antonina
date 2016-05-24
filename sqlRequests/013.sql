select users.name, count(idUsers) as CountMessagesToday
from (messages left join users on users.id = messages.idUsers)
where date = curdate()
group by name
having CountMessagesToday > 3;
