
var table;
var q=0;

                    
  

// Pipelining function for DataTables. To be used to the `ajax` option of DataTables
//         
$(document).ready(function(){  
$.fn.dataTable.pipeline = function ( opts ){
    // Configuration options 
    var conf = $.extend( {
        pages: 1,     // number of pages to cache
        url: '',      // script url
        data: null,   // function or object with parameters to send to the server
                      // matching how `ajax.data` works in DataTables
        method: 'GET' // Ajax HTTP method
    }, opts );
 
    // Private variables for storing the cache

    return function ( request, drawCallback, settings ) {
      var ajax          = false;
      var requestStart  = request.start;
      var drawStart     = request.start;
      var requestLength = request.length;
      var requestEnd    = requestStart + requestLength;
 
      if ( ajax ) {
            // Need data from the server
          
                requestStart = requestStart - requestLength;
 
                if ( requestStart < 0 ) {
                    requestStart = 0;
                }
            }
                 
           request.start = requestStart;
           request.length = requestLength;
            settings.jqXHR = $.ajax( {
                "type":     conf.method,
                "url":      conf.url,
                "data":     requestParam(request),
                "dataType": "json",
                "cache":    false,
                "success":  function ( json,textStatus, requestj ) {
 
                    requestj.getResponseHeader('recordsTotal');
                    requestj.getResponseHeader('recordsFiltered');
          
                    drawCallback({
                      data: json,
                      recordsTotal:  requestj.getResponseHeader('recordsTotal'),    
                      recordsFiltered: requestj.getResponseHeader('recordsFiltered')
                    });
                }
            } );
        }
     
    };

$.fn.dataTable.Api.register( 'clearPipeline()', function () {
    return this.iterator( 'table', function ( settings ) {
  
    } );
} );

 
table = $("#feature_table").DataTable({
             "processing": true,
             "serverSide": true,
             "searching":true,
              "ajax": $.fn.dataTable.pipeline({
              "url": '../../../../RESTful/api/service/Features',
              "deferRender": true,
              "dataSrc": "",
              "type": "GET",
              "pages": 1
             
        }),
        "columns": [
            
            {"data": "id", 'id':'id', "title":'ID'},
            {"data": "renderingEngine", "title":"Rendering Engine"},
            {"data": "browser", "title":"Browser"},
            {"data": "platform", "title":"Platform"},
            {"data": "engineVersion", "title":"Engine Version"},
            {"data": "cssGrade", "title":"Css Grade"},
            {"targets": -1,"data": null,"defaultContent": "<a id = 'edit' class='btn btn-sm btn-primary'> <i class='glyphicon glyphicon-pencil'> </i> Edit</a>&nbsp<a id = 'delete' class='btn btn-sm btn-danger'><i class='glyphicon glyphicon-trash'></i> Delete</a> ","title":"Action"}    
        ]
        

      }); 
  

  $('#feature_table tbody').on( 'click', '#delete', function () {
      
        var data = table.row( $(this).parents('tr') ).data();
        var id = data["id"];
        if (window.confirm('You really wanna delete this feature?'))
        {
        deleteFeature(id);
        table.row( $(this).parents('tr') ).remove().draw();
        table.clearPipeline().draw();
         // They clicked Yes
        }
        else
        {
    // They clicked no
        }
     
    } );
   var data;
   $('#feature_table tbody').on( 'click', '#edit', function () {
          data = table.row( $(this).parents('tr') ).data();
            $('[name="id"]').val(data["id"]);
            $('[name="renderingEngine"]').val(data["renderingEngine"]);
            $('[name="browser"]').val(data["browser"]);
            $('[name="platform"]').val(data["platform"]);
            $('[name="engineVersion"]').val(data["engineVersion"]);
            $('[name="cssGrade"]').val(data["cssGrade"]);
          $('#modal_form').modal('show');
          $('.modal-title').text('Edit Features');
          save_method = 'update';

    });

    $('#btnSave').click(function(){
    var dataType;
    var url;
    var data = {
        renderingEngine: $('[name="renderingEngine"]').val(),
        browser:$('[name="browser"]').val(),
        platform:$('[name="platform"]').val(),
        engineVersion:$('[name="engineVersion"]').val(),
        cssGrade:$('[name="cssGrade"]').val()
      }
     var jsdata = JSON.stringify(data);   

     if(save_method =='add'){
      $('.modal-title').text('Add Features');
      url = "../../../../RESTful/api/service/Feature";    
      dataType = "POST"; 
     }
     else{
      $('.modal-title').text('Edit Features');  
      url = "../../../../RESTful/api/service/Feature/"+$('[name="id"]').val();   
      dataType = "PUT";
     }
  $('#btnSave').text('saving...'); //change button text
  $('#btnSave').attr('disabled',true); //set button disable
  $.ajax({
        url : url,
        contentType: "application/json",
        data: jsdata,
        type: dataType,
      
        success: function(data)
        {   
           
           
             $('#btnSave').text('save'); //change button text
             $('#btnSave').attr('disabled',false); //set button enable
             $('#modal_form').modal('hide');
                  table.clearPipeline().draw();
        },
        error: function (jqXHR, textStatus, errorThrown)
        {
            alert('Error with update or create data');
            $('#btnSave').text('save'); //change button text
            $('#btnSave').attr('disabled',false); //set button enable
        }
     });
    });
     function deleteFeature($id) {
    $.ajax({
        "url": '../../../../RESTful/api/service/Feature/'+$id,
            "deferRender": true, 
            "type": "DELETE"
      
    });
  }


  $('#reload').click(function(){
    table.clearPipeline().draw();
});
});


$('#add_feature').click(function(){

    save_method = 'add';
    $('#form')[0].reset(); // reset form on modals
    $('.form-group').removeClass('has-error'); // clear error class
    $('.help-block').empty(); // clear error string
    $('#modal_form').modal('show'); // show bootstrap modal
    $('.modal-title').text('Add Feature'); // Set Title to Bootstrap modal title


});
 



