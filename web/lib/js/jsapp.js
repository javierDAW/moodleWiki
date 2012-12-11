/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = initForm;
function initForm () {
    reloj();
    setInterval( "reloj()", 1000 );
    
    var ids = document.getElementsByName('accion');
    for(var i=0;i<ids.length;i++){
        ids[i].onclick = function () {
            document.getElementById('idDoc').value = this.id;
        };
    }
    
    document.getElementById("publicar").onclick = function () {
        errores();
    }
    
}

function putData(list){
    if(list.length != 0){
        formulario.titulo.value = list[0];
        formulario.contenido.value = list[1];
        formulario.privado.value = list[2];
        formulario.etiquetas.value = list[3];
        formulario.idDoc.value = list[4];
        focmulario.accion.value = 'actualizar';
    } 
}

function errores(){
    var formul = document.formulario;
        var errores = 0;
        if(formul.titulo.value.trim() == ''){ 
            formul.titulo.style.border='1px solid red';
            document.getElementById('titerror').innerHTML = 'Debe escribir un título';
            errores++;
        } else {
            formul.titulo.style.border='';
            document.getElementById('titerror').innerHTML = '';
        }

        if(formul.contenido.value.trim() == '' 
            || formul.contenido.value == '<p><br></p>'){
            formul.contenido.parentNode.style.border='1px solid red';
            document.getElementById('conterror').innerHTML = 'Debe escribir un contenido';
            errores++;
        } else {
            formul.contenido.parentNode.style.border='';
            document.getElementById('conterror').innerHTML = '';
        }
        if(formul.etiquetas.value.trim() == ''){
            formul.etiquetas.style.border='1px solid red';
            document.getElementById('eterror').innerHTML = 'Debe escribir una etiqueta';
            errores++;
        } else{
            formul.etiquetas.style.border='';
            document.getElementById('eterror').innerHTML = '';
        }
                    
        if(errores == 0) formul.submit();
}

lalala = true;

function reloj(){
    ahora = new Date()
 
    hora = ahora.getHours()
    minuto = ahora.getMinutes()
         
    str_minuto = String (minuto)
    if (str_minuto.length == 1){
        minuto = "0" + minuto
    }
 
    str_hora = new String (hora)
    if (str_hora.length == 1){
        hora = "0" + hora
    }
 
    if(lalala == false) {
        time = hora + " &nbsp; " + minuto; 
        lalala = true;
    } else {
        time = hora + " : " + minuto;
        lalala = false;
    }
 
    document.getElementById('reloj').innerHTML = time;
}