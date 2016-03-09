/** Allow to add, delete and edit messages in a  history
 * Created by Антонина on 01.03.16.
 */

function run() {
    var appContainer = document.getElementsByClassName('chatContainer')[0];
    appContainer.addEventListener('click', delegateEvent);
    appContainer.addEventListener('click', delegateEvent);
}


var name = "Guest";

function delegateEvent(evtObj) {
    if (evtObj.type === 'click' && evtObj.target.classList.contains('buttonOk')) {
        onOkButtonClick(evtObj);
    }
    if (evtObj.type === 'click' && evtObj.target.classList.contains('buttonSend')) {
        onSendButtonClick(evtObj);
    }
}

function onOkButtonClick() {
    var previousName = name;
    var nameItem = document.getElementById('name');
    if (!nameItem.value)
        return;
    name = nameItem.value;
    nameItem.style.color = 'blue';
    nameItem.style.fontStyle = 'italic';
    var inputMessage = document.getElementById('message');
    inputMessage.setAttribute('placeholder', name + ', enter you message');
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
    var table = document.getElementById('tableMessage').getElementsByTagName('tbody')[0];
    var rowCount = table.rows.length;
    for (var i = 0; i < rowCount; i ++) {
        var row = table.rows[i];
        if (row.cells[0].innerHTML === previousName) {
            row.cells[0].innerHTML = name;
        }
    }
}

function onSendButtonClick() {
    var message = document.getElementById('message');
    addMessage(message.value);
    message.value = '';
}

function findIndexMessage(id) {
    var table = document.getElementById('tableMessage').getElementsByTagName('tbody')[0];
    var rowCount = table.rows.length;
    for (var i = 0; i < rowCount; i ++) {
        var row = table.rows[i];
        if (row.getAttribute('id') === (id.substring(0, id.length - 1) + 'tr')) {
            return i;
        }
    }
    return null;
}

function setDoneActionIcon(i, src) { //show that message is removed or edited
    var table = document.getElementById('tableMessage').getElementsByTagName('tbody')[0];
    var previousRow = table.rows[i];
    previousRow.cells[2].appendChild(createIcon(src));
}


function deleteMessage(id) {
    var table = document.getElementById('tableMessage').getElementsByTagName('tbody')[0];
    var i = findIndexMessage(id);
    if (i != null ) {
        setDoneActionIcon(i - 1, 'http://icons.iconarchive.com/icons/icons8/ios7/256/Messaging-Trash-icon.png');
        table.deleteRow(i);
    }
}

function editMessage(id) {
    var table = document.getElementById('tableMessage').getElementsByTagName('tbody')[0];
    var i = findIndexMessage(id);
    if (i != null) {
        var dialog = document.getElementById('window');
        dialog.show();
        document.getElementById('exit').onclick = function () {
            dialog.close();
        };
        document.getElementById('ok').onclick = function () {
            var newMessage = document.getElementById('newText');
            table.rows[i].cells[0].innerHTML = newMessage.value;
            setDoneActionIcon(i - 1, "http://www.free-icons-download.net/images/edit-icon-61879.png");
            dialog.close();
        }
    }
}

function createInputTextOfDialog() {
    var inputText = document.createElement('input');
    inputText.setAttribute('id', 'newText');
    inputText.setAttribute('type', 'text');
    inputText.classList.add('inputNewMessage');
    inputText.setAttribute('placeholder', 'Enter message');
    return inputText;
}

function createButtonOkOfDialog(){
    var buttonOk = document.createElement('button');
    buttonOk.setAttribute('id', 'ok');
    buttonOk.setAttribute('type', 'button');
    buttonOk.classList.add('buttonOk');
    buttonOk.appendChild(document.createTextNode('Ok'));
    return buttonOk;
}

function createButtonExitOfDialog() {
    var buttonExit = document.createElement('button');
    buttonExit.setAttribute('id', 'exit');
    buttonExit.classList.add('buttonOk');
    buttonExit.appendChild(document.createTextNode('Exit'));
    return buttonExit;
}

var dialog;
function createDialogBox() {
    dialog = document.createElement('dialog');
    dialog.setAttribute('id', 'window');
    dialog.classList.add('dialogBoxMessage');
    dialog.appendChild(createInputTextOfDialog());
    dialog.appendChild(createButtonOkOfDialog());
    dialog.appendChild(createButtonExitOfDialog());
    return dialog;
}

function createCellName() {
    var cellName = firstRow.insertCell(0);
    cellName.appendChild(document.createTextNode(name));
    cellName.classList.add('outputName');
}

function createCellAction() {
    var table = document.getElementById('tableMessage').getElementsByTagName('tbody')[0];
    var cellActions = firstRow.insertCell(1);
    cellActions.appendChild(createDialogBox());
    var iconEdit = createIcon('http://findicons.com/files/icons/2443/bunch_of_cool_bluish_icons/256/edit.png', 'editMessage(id)', (table.rows.length + 1) + 'e');
    var iconDelete = createIcon('http://www.edge-online.org/wp-content/uploads/2014/11/garbage.png', 'deleteMessage(id)', (table.rows.length + 1) +'d');
    cellActions.classList.add('outputAction');
    cellActions.appendChild(iconEdit);
    cellActions.appendChild(iconDelete);
}

function createCellDoneActions() {
    var cellDoneAction = firstRow.insertCell(2);
}

function createCellTime() {
    var cellTime = firstRow.insertCell(3);
    var d = new Date();
    cellTime.appendChild(document.createTextNode(d.toLocaleTimeString()));
    cellTime.classList.add('outputTime');
}

function createCellText(value) {
    var cellMessage = secondRow.insertCell(0);
    cellMessage.appendChild(document.createTextNode(value));
    cellMessage.classList.add('outputMessage');
}

function createCellError() { // no functional part just example
    var table = document.getElementById('tableMessage').getElementsByTagName('tbody')[0];
    var cellError = secondRow.insertCell(1);
    if (table.rows.length === 2) {
        var iconError = createIcon('https://cdn0.iconfinder.com/data/icons/shift-free/32/Error-128.png', null, 0);
        cellError.appendChild(iconError);
    }
}

var firstRow;
var secondRow;

function addMessage(value) {
    if (!value)
        return;
    var table = document.getElementById('tableMessage').getElementsByTagName('tbody')[0];
    firstRow = table.insertRow(table.rows.length);
    secondRow = table.insertRow(table.rows.length);
    secondRow.setAttribute('id', (table.rows.length + 1) + 'tr');
    createCellName();
    createCellAction();
    createCellDoneActions();
    createCellTime();
    createCellText(value);
    createCellError();
}

function createIcon(src, functn, id) {
    var icon = document.createElement('img');
    icon.setAttribute('src', src);
    icon.setAttribute('width', '20');
    icon.setAttribute('height', '20');
    icon.setAttribute('onclick', functn);
    icon.setAttribute('id', id);
    return icon;
}

