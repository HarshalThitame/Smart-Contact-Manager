console.log("This is script");

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};






//check Password
$('#upassword').keyup(function() {
	if ($(this).val().length >= 4) {
		document.getElementById("message").style.color = 'green';
		document.getElementById("uconfirmpassword").removeAttribute("disabled")

		document.getElementById('message').innerHTML = "Strong Password !";
	} else {
		document.getElementById("message").style.color = 'red';
		$('#regbtn').addClass('disabled');
		document.getElementById('message').innerHTML = "Weak Password !";
		document.getElementById("uconfirmpassword").setAttribute("disabled", "")

	}
})


//check Confirm password

$('#uconfirmpassword').keyup(function() {
	if ($(this).val() == $('#upassword').val()) {
		document.getElementById("message2").style.color = 'green';

		document.getElementById('message2').innerHTML = "Password match !";
		$('#regbtn').removeClass('disabled');
	} else {
		document.getElementById("message2").style.color = 'red';

		document.getElementById('message2').innerHTML = "Passowrd does not match !";
			$('#regbtn').addClass('disabled');
	
	}
});

