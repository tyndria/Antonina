select users.name from (users inner join messages on users.id = messages.idUsers)
group by name
having count(messages.idUsers) >= 3;
