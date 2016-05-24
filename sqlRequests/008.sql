select users.name, messages.text, messages.date 
from (users right join messages on idUsers = id)
where id = 8
order by date;
