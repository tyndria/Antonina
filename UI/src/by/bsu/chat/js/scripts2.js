/**
 * Created by Антонина on 15.03.16.
 */
var messageTable = [];

var name = "Guest";
function run() {
    var appContainer = document.getElementsByClassName('chatContainer')[0];
    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('click', delegateEvent);
    var allMessages = loadTasks();
    if (allMessages != null) {
        render(allMessages);
    }
}

function delegateEvent(evtObj) {
    if (evtObj.type === 'click' && evtObj.target.classList.contains('buttonOk')) {
        onOkButtonClick(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('buttonSend')) {
        onSendButtonClick(evtObj);
    }
}

function onOkButtonClick() {
    var nameItem = document.getElementById('name');
    if (!nameItem.value)
        return;
    name = nameItem.value;

    nameItem.style.color = 'blue';
    nameItem.style.fontStyle = 'italic';

    var inputMessage = document.getElementById('message');
    inputMessage.setAttribute('placeholder', name + ', enter you message');
}

function uniqueId() {
    var date = Date.now();
    var random = Math.random() * Math.random();

    return Math.floor(date * random);
}

function newMessage(name, text, time, deleted, edited, wasEdited) {
    return {
        name: '' + name,
        text: '' + text,
        timestamp: '' + time,
        deleted: !!deleted,
        edited: !!edited,
        wasEdited: !!wasEdited,
        id: uniqueId()
    };
}

function render(allMessages) {
    for (var i = 0; i < allMessages.length; i ++) {
        renderMessage(allMessages[i]);

        messageTable.push(allMessages[i]);
    }
}

function elementFromTemplate(str) {
    var template = document.getElementById(str);

    return template.firstElementChild.cloneNode(true);
}

function renderMessage(message) {
    var list = document.getElementsByClassName('listMessage')[0];

    var element = elementFromTemplate('messageTemplate');

    renderMessageState(element, message);
    list.appendChild(element);
}

function renderMessageState(element, message) {
    element.setAttribute('data-message-id', message.id);

    if (message.deleted) {

        if (message.edited) {
            element.removeChild(element.lastChild);
        }

        element.firstChild.textContent = "[" + message.timestamp + "]" + " " + message.name + " ";
        element.lastChild.textContent = "";
        element.appendChild(createIcon('http://icons.iconarchive.com/icons/icons8/ios7/256/Messaging-Trash-icon.png'));
    }
    else {
        element.firstChild.textContent = "[" + message.timestamp + "]"+ " " + message.name + " ";
        element.lastChild.textContent = message.text + " ";

        if (message.edited) {
            element.appendChild(createIcon('http://www.free-icons-download.net/images/edit-icon-61879.png'));
            message.wasEdited = true;
        }
    }
}



function onSendButtonClick() {
    var textMessage = document.getElementById('message');
    var message = newMessage(name, textMessage.value, getTime(), false, false, false);
    messageTable.push(message);
    renderMessage(message);
    saveTasks(messageTable);
}

function getTime() {
    var d = new Date();
    return d.toLocaleTimeString();
}

function saveTasks(messageToSave) {
    if (typeof (Storage) == "undefined") {
        alert("Storage is not accessible")
        return;
    }
    localStorage.setItem("history", JSON.stringify(messageToSave));
}

function loadTasks() {
    if (typeof (Storage) == "undefined") {
        alert("Storage is not accessible")
        return;
    }
    var items = localStorage.getItem("history");
    return items && JSON.parse(items);
}

function deleteMessage(element) {
    var currentElement = element.parentNode;
    var index = indexByElement(currentElement, messageTable);
    var message = messageTable[index];
    if (!message.deleted) {
        message.deleted = true;
        renderMessageState(currentElement, message);
        saveTasks(messageTable);
    }
}

function indexByElement(element, messages){
    var id = element.attributes['data-message-id'].value;
    return messages.findIndex(function(item) {
        return item.id == id;
    });
}

function createIcon(src) {
    var icon = document.createElement('img');
    icon.setAttribute('src', src);
    icon.setAttribute('width', '20');
    icon.setAttribute('height', '20');
    return icon;
}

function editMessage(element) {
    var currentElement = element.parentNode;
    var index = indexByElement(currentElement, messageTable);
    var message = messageTable[index];
    if (!message.deleted) {
        var dialog = elementFromTemplate('dialogBoxTemplate');
        currentElement.appendChild(dialog);
        dialog.show();
        document.getElementById('exit').onclick = function () {
            dialog.close();
        };
        document.getElementById('ok').onclick = function () {
            var newMessage = document.getElementById('newText');
            message.text = newMessage.value;
            message.edited = true;
            dialog.close();
            currentElement.removeChild(dialog);
            if (message.wasEdited)
                currentElement.removeChild(currentElement.lastElementChild);
            saveTasks(messageTable);
            renderMessageState(currentElement, message);
        }
    }
}

function editName() {
    var previousName = name;
    var currentName = document.getElementById('name');
    if (!currentName.value)
        return;
    name = currentName.value;
    currentName.style.color = 'blue';
    currentName.style.fontStyle = 'italic';
    var inputMessage = document.getElementById('message');
    inputMessage.setAttribute('placeholder', name + ', enter you message');
    for (var i = 0; i < messageTable.length; i ++) {
        if (messageTable[i].name == previousName) {
            messageTable[i].name = currentName.value;
        }
    }
    var list = document.getElementById('listMessage');
    var children = list.children;
    for (var i = 0; i < children.length; i++) {
        var namePlusTimestamp = '';
        namePlusTimestamp = children[i].firstChild.textContent;
        var index = namePlusTimestamp.search(']');
        var nameFromString = namePlusTimestamp.substring((index + 2), namePlusTimestamp.length - 1);
        if (nameFromString == previousName) {
            namePlusTimestamp = namePlusTimestamp.replace(nameFromString, currentName.value);
            children[i].firstChild.textContent = namePlusTimestamp;
        }
    }
    saveTasks(messageTable);
}