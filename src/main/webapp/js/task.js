$(document).ready(function(){
	$('.btn-delete').click(function(){
		let id = $(this).attr('task-id')
		let btn = $(this)
		$.ajax({
		  method: "GET",
		  url: `http://localhost:8080/crm06/api/task?id=${id}`
		  //data: { name: "John", location: "Boston" }
		}).done(function( result ) {
		  if(result.data) {
			  btn.closest('tr').remove();
		  } else {
			  alert('xoa that bai')
		  }
	  })
	})
	
})