/**Use ajax
 * Created by Антонина on 10.04.16.
 */

var Application = {
    mainUrl: 'http://localhost:8080/chat',
    messageList: [],
    token: 'TN11EN'
};

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

    loadMessages(function () {
        render(Application);
    });

    setUserName();
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

function editMessage(element, done) {
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

        if (message.wasEdited == 'true') {
            element.removeChild(element.lastChild);
        }
        ajax('PUT', Application.mainUrl, JSON.stringify(message), function () {
            done(element, message);
        });
    }
}

function deleteMessage(id, done) {
    var url = Application.mainUrl + '?msgId=' + id;

    ajax('DELETE', url, null, function () {
        done();
    });
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
    deleteMessage(id, function () {
        renderMessageState(currentElement, message);
    });
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

    editMessage(currentElement, function () {
        renderMessageState(currentElement, message);
    });
}

function loadMessages(done) {
    var url = Application.mainUrl + '?token=' + Application.token;
    ajax('GET', url, null, function (responseText) {
        var response = JSON.parse(responseText);

        Application.messageList = response.messages;
        Application.token = response.token;
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
            return;
        }
        continueWith(xhr.responseText);
    };

    var errorIcon = document.getElementById('errorIcon');

    xhr.onerror = function (e) {
        errorIcon.style.visibility = (errorIcon.style.visibility == "visible") ? "hidden" : "visible";
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

    addMessage(message, function () {
        renderMessage(message);
    });
}

function addMessage(message, done) {
    ajax('POST', Application.mainUrl, JSON.stringify(message), function () {
        Application.messageList.push(message);
        done();
    });
}

function textValue() {
    var messageTextElement = document.getElementById('message');
    var messageTextValue = messageTextElement.value;

    messageTextElement.value = '';

    return messageTextValue;
}

function renderMessage(message) {
    var list = document.getElementsByClassName('listMessage')[0];

    var child = elementFromTemplate('messageTemplate');
    renderMessageState(child, message);
    list.appendChild(child);
}

function render(root) {
    for (var i = 0; i < root.messageList.length; i++) {
        renderMessage(root.messageList[i]);
    }
}

function caseDeletedMessage(element, message) {
    if (message.edited == 'true') {
        element.removeChild(element.lastChild);
    }
    element.firstChild.textContent = "[" + message.timestamp + "]" + " " + message.name + " ";
    element.lastChild.textContent = "";
    element.appendChild(createIcon('http://icons.iconarchive.com/icons/icons8/ios7/256/Messaging-Trash-icon.png'));
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
            element.appendChild(createIcon('http://www.free-icons-download.net/images/edit-icon-61879.png'));
            message.wasEdited = 'true';
        }
    }
}


function createIcon(src) {
    var icon = document.createElement('img');

    icon.setAttribute('src', src);
    icon.setAttribute('width', '20');
    icon.setAttribute('height', '20');

    return icon;
}

function elementFromTemplate(str) {
    var template = document.getElementById(str);

    return template.firstElementChild.cloneNode(true);
}


