window.com_company_qrcode_web_ui_AutocompleteExtension = function(){
    var connector = this;
    var tribute;
    var triggerChar = String.fromCharCode(8625);/*decimal Стрелка вверх-вправо 90 градусов*/
    this.onStateChange = function(){
        var templates = this.getState().templates;
        var parentId = connector.getParentId();
        var parent = connector.getElement(parentId);
        var element = parent.querySelector('textarea')||parent.querySelector('input')||parent;
        var templates = this.getState().templates;
        if (!templates||templates.length===0){
            return;}
        tribute = new Tribute({
            values: templates.map(function(t){
                return {key: t.code, value: t.content};
            }),
            trigger: triggerChar,
            requireLeadingSpace: false,
            selectTemplate:function(item){
                return item.original.value;
            },
            menuItemTemplate:function(item){
                return '<span style="font-weight:bold">'
                +item.original.key+'</span>'+'<span style="color:gray">'
                +item.original.value.substring(0,20)+'...</span>';
            }});
        tribute.attach(element);
        element.addEventListener('keydown', function(e){
            if(e.key==='F2'){
                console.log("Нажата клавиша F2")
                e.preventDefault();
                var startPos = element.selectionStart;
                var endPos = element.selectionEnd;
                var text = element.value;
                element.value = text.substring(0, startPos)
                +triggerChar
                +text.substring(endPos);
                var newCursorPos = startPos+triggerChar.length;
                element.setSelectionRange(newCursorPos, newCursorPos);
                element.dispatchEvent(new Event('input', { bubbles: true }));
                tribute.showMenuFor(element);
            }
            console.log("Проверка не сработала")
        })
    };
};