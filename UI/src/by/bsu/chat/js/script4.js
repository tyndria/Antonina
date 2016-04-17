/**use ajax, asyn cycle
 * Created by Антонина on 15.04.16.
 */


var isConnected = void 0;

var Application = {
    mainUrl: 'http://localhost:8080/chat',
    messageList: [],
    token: 'TN11EN'
};

function seconds(value) {
    return Math.round(value * 1000);
}

function whileConnected() {
    isConnected = setTimeout(function () {
        loadMessages(function () {
            render(Application);
            whileConnected()
        });
    }, seconds(1));
}

function uniqueId() {
    var date = Date.now();
    var random = Math.random() * Math.random();

    return Math.floor(date * random);
}

function getTime() {
    var d = new Date();

    return d.toLocaleTimeString();
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

var name = "Guest";
function run() {
    var appContainer = document.getElementsByClassName('chatContainer')[0];

    appContainer.addEventListener('click', delegateEvent);

    setUserName();

    connect();
}

function connect() {
    if (isConnected)
        return;

    whileConnected();
}

function setUserName() {
    if (name === 'null' || name == 'undefined')
        name = "Guest";

    var inputName = document.getElementById('name');
    inputName.setAttribute('placeholder', name);

    var inputMessage = document.getElementById('message');
    inputMessage.setAttribute('placeholder', name + ', enter you message');
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

function editMessage(element) {
    var id = idFromElement(element);

    var index = indexById(Application.messageList, id);

    var message = Application.messageList[index];
    var dialog = document.getElementById("overlay");
    dialog.style.visibility = (dialog.style.visibility == "visible") ? "hidden" : "visible";

    var inputNewMessage = document.getElementById('newText');
    inputNewMessage.focus();

    document.getElementById('exit').onclick = function () {
        inputNewMessage.value = "";
        dialog.style.visibility = (dialog.style.visibility == "visible") ? "hidden" : "visible";
    };

    document.getElementById('ok').onclick = function () {
        message.text = inputNewMessage.value;
        message.edited = 'true';

        inputNewMessage.value = "";
        dialog.style.visibility = (dialog.style.visibility == "visible") ? "hidden" : "visible";

        ajax('PUT', Application.mainUrl, JSON.stringify(message));
    }
}

function deleteMessage(id) {
    var url = Application.mainUrl + '?msgId=' + id;

    ajax('DELETE', url, null);
}

function indexById(list, id) {
    return list.findIndex(function (item) {
        return item.id == id;
    });
}

function onItemClickToDelete(element) {
    var currentElement = element.parentNode;
    var id = idFromElement(currentElement);

    var index = indexById(Application.messageList, id);

    var message = Application.messageList[index];

    message.deleted = 'true';
    deleteMessage(id);
}

function idFromElement(element) {
    return element.attributes['data-message-id'].value;
}

function onItemClickToEdit(element) {
    var currentElement = element.parentNode;

    var id = idFromElement(currentElement);

    var index = indexById(Application.messageList, id);

    var message = Application.messageList[index];

    message.edited = 'true';
    if (message.deleted == 'true') {
        return;
    }

    editMessage(currentElement);
}

function loadMessages(done) {
    var url = Application.mainUrl + '?token=' + Application.token;
    ajax('GET', url, null, function (responseText) {
        var response = JSON.parse(responseText);
        Application.messageList = response.messages;
        done();
    });
}


function ajax(method, url, data, continueWith) {
    var xhr = new XMLHttpRequest();

    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {
        if (xhr.readyState != 4) {
            return
        }
        if (xhr.status != 200) {
            return;
        }
        if (isError(xhr.responseText)) {
            isConnected = void 0;
            connect();
            return;
        }
        continueWith(xhr.responseText);
    };

    var errorIcon = document.getElementById('errorIcon');

    xhr.onerror = function (e) {
        errorIcon.style.visibility = (errorIcon.style.visibility == "visible") ? "hidden" : "visible";
        isConnected = void 0;
        connect();
    };
    errorIcon.style.visibility = "hidden";


    xhr.send(data);
}

function isError(text) {
    if (text == "")
        return false;

    try {
        var obj = JSON.parse(text);
    } catch (ex) {
        return true;
    }

    return !!obj.error;
}

function onSendButtonClick() {
    var textMessage = textValue();
    if (textMessage == "" || textMessage == null)
        return;

    var message;

    if (Application.messageList.length == 0) {
        message = newMessage(name, textMessage, getTime(), false, false, false);
    }
    else {
        message = newMessage(name, textMessage, getTime(), false, false, false);
    }

    addMessage(message);
}

function addMessage(message) {
    ajax('POST', Application.mainUrl, JSON.stringify(message), function () {
        Application.messageList.push(message);
    });
}

function textValue() {
    var messageTextElement = document.getElementById('message');
    var messageTextValue = messageTextElement.value;

    messageTextElement.value = '';

    return messageTextValue;
}

function updateList(list, messageMap) {
    var children = list.children;
    var notFound = [];

    for (var i = 0; i < children.length; i++) {
        var child = children[i];
        var id = child.attributes['data-message-id'].value;
        var item = messageMap[id];

        if (item == null) {
            notFound.push(child);
            continue;
        }

        renderMessageState(child, item);

        messageMap[id] = null;
    }

    return notFound;
}

function appendToList(list, messageList, messageMap) {
    for (var i = 0; i < messageList.length; i++) {
        var message = messageList[i];

        if (messageMap[message.id] == null) {
            continue;
        }
        messageMap[message.id] = null;

        var child = elementFromTemplate('messageTemplate');

        renderMessageState(child, message);
        list.appendChild(child);
    }
}

function render(root) {
    var list = document.getElementsByClassName('listMessage')[0];

    var messagesMap = root.messageList.reduce(function (accumulator, message) {
        accumulator[message.id] = message;

        return accumulator;
    }, {});

    updateList(list, messagesMap);

    appendToList(list, root.messageList, messagesMap);
}

function caseDeletedMessage(element, message) {
    element.firstChild.textContent = "[" + message.timestamp + "]" + " " + message.name + " ";
    element.lastChild.textContent = "";
    var deleteIcon = element.getElementsByClassName('deleteIcon')[0];
    deleteIcon.style.visibility = "visible";
}

function renderMessageState(element, message) {
    element.setAttribute('data-message-id', message.id);

    if (message.deleted == 'true') {
        caseDeletedMessage(element, message);
    }
    else {
        element.firstChild.textContent = "[" + message.timestamp + "]" + " " + message.name + " ";
        element.lastChild.textContent = message.text + " ";

        if (message.edited == 'true') {
            var editIcon = element.getElementsByClassName('editIcon')[0];
            editIcon.style.visibility = "visible";
            message.wasEdited = 'true';
        }
    }
}


function elementFromTemplate(str) {
    var template = document.getElementById(str);

    return template.firstElementChild.cloneNode(true);
}


