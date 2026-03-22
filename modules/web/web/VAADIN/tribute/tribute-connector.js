window.com_company_qrcode_web_ui_AutocompleteExtension = function(){
    var connector = this;
    var triggerChar = String.fromCharCode(8625);/*decimal Стрелка вверх-вправо 90 градусов*/
    var lastTriggerPos = -1;
    var tribute = null;
    this.onStateChange = function(){
        var templates = this.getState().templates;
        var parent = connector.getElement(connector.getParentId());
        var element = parent.querySelector('textarea')||parent.querySelector('input')||parent;
        if (!templates||templates.length===0){return 1;}
        if(tribute){
            tribute.collection[0].values = templates.map(function(t){
                return {key:t.code, value:t.content};
            });
            return;
        }
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
        if(tribute.isActive) return;
            if(e.key==='F2'){
                if(e.repeat){
                    e.preventDefault();
                    return;
                }
                if(element.value.includes(triggerChar)){
                    e.preventDefault();
                    return;
                }
                e.preventDefault();
                var startPos = element.selectionStart;
                var text = element.value;
                lastTriggerPos = startPos;
                element.value = 
                text.substring(0, startPos)
                +triggerChar
                +text.substring(element.selectionEnd);
                var newCursorPos = startPos+triggerChar.length;
                element.setSelectionRange(newCursorPos, newCursorPos);
                element.dispatchEvent(new Event('input', { bubbles: true }));
                tribute.showMenuFor(element);
            }
        });
        element.addEventListener('tribute-active-false', function(e){
            setTimeout(function(){
                if(tribute.isActive) return;
                if(lastTriggerPos!==-1){
                    var text = element.value;
                    if(text.charAt(lastTriggerPos)===triggerChar){
                        element.value = text.slice(0, lastTriggerPos) + text.slice(lastTriggerPos+1);
                        var currentCursor = element.selectionStart;
                        var newCursor = (currentCursor > lastTriggerPos) ? currentCursor - 1 : currentCursor;
                        element.setSelectionRange(newCursor, newCursor);
                        element.dispatchEvent(new Event('input', { bubbles: true }));
                    }
                    lastTriggerPos=-1;
                }
            }, 200);
        });
        element.addEventListener('tribute-replaced', function(){
            lastTriggerPos = -1;
        });
    };
};