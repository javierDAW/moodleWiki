<%-- 
    Document   : redactor
    Created on : 16-nov-2012, 2:40:13
    Author     : María Galbis
--%>

<script type="text/javascript" src="lib/js/jquery-1.8.2.min.js"></script>
<script src="lib/js/redactor.js"></script>
<script type="text/javascript">
$(document).ready(
    function(){
        $('#textarea').redactor({
            wym: true,
            fixed: true,
                buttons:  ['html', '|', 'formatting', '|', 'bold', 'italic', 'underline', 'deleted', 
                    '|',  'unorderedlist', 'orderedlist', 'outdent', 'indent', '|', 'image', 
                    'table', 'link', '|', 'alignment', '|', 'horizontalrule']
        });
    }
);
</script>
