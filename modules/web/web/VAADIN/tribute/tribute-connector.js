console.log("Hello1");
window.com_company_qrcode_web_ui_AutocompleteExtension = function(){
    console.log("Hello2");
    var connector = this;
    console.log("Hello3");
    var element = connector.getElement(connector.getParentId());
    console.log("Hello4");
    var tribute;
    console.log("Hello5");
    this.onStateChange = function(){
        var templates = this.getState().templates;
        var parentId = connector.getParentId();
        var parent = connector.getElement(parentId);
        var element = parent.querySelector('textarea') || parent.querySelector('input') || parent;
        console.log("ДАННЫЕ:", templates);
        console.log("ЦЕЛЕВОЙ ЭЛЕМЕНТ:", element);
        console.log("Hello6");
        var templates = this.getState().templates;
        console.log("Hello7");
        if (!templates || templates.length === 0){
            console.log("NULL СПИСОК ШАБЛОНОВ ПУСТ");
            return;}
        console.log("Hello8");
        tribute = new Tribute({values: templates.map(function(t){return {key: t.code, value: t.content};}),trigger: '/',selectTemplate: function(item){return item.original.value;},menuItemTemplate: function(item){return '<span style="font-weight:bold">' + item.original.key + '</span>' + '<span style="color:gray">' + item.original.value.substring(0,20) + '...</span>';}});
        console.log("Hello9");
        tribute.attach(element);
        console.log("Hello10");
    };
    console.log("Hello11");
};