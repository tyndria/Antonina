select users.name, messages.date, messages.text
from (messages inner join users on id = idUsers)
where text like '%are_you%';