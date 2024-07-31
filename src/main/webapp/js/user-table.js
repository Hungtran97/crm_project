$(document).ready(function(){
    $('#example').DataTable();

    // Event delegation to handle click events on delete buttons
    $(document).on('click', '.btn-delete', function(event){
        event.preventDefault(); // Prevent the default action of the anchor tag

        let id = $(this).attr('user-id');
        let btn = $(this);

        $.ajax({
            method: "GET",
            url: `http://localhost:8080/crm06/api/user?id=${id}`,
            success: function(result) {
                if(result.data) {
                    btn.closest('tr').remove();
                } else {
                    alert('Xóa thất bại. Bạn không có quyền');
                }
            },
            error: function(xhr, status, error) {
                console.error("Error deleting user:", error);
                alert('Đã xảy ra lỗi khi xóa người dùng.');
            }
        });
    });
});
