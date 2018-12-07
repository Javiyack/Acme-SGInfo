$(document).ready(function() {
	// Get the Sidebar
	var mySidebar = document.getElementById("mySidebar");

	// Get the DIV with overlay effect
	var overlayBg = document.getElementById("myOverlay");

	var mybtn = document.getElementsByClassName("testbtn")[0];

	// Slideshows
	var slideIndex = 1;
	ajaxSearch(document.getElementById("combo"), "/Acme-CRM")

});

function selectedCheck(element){
	alert(element);
}

function ajaxCheck(element, contextPath) {
	id = element.value;
	url = contextPath + "/jquery/hostCheck.do?id=" + id;
	$.get(url, function(allowed, status) {
		alert(allowed, status);
		document.getElementById("MANAGER").disabled = !allowed;
		document.getElementById("TECHNICIAN").disabled = !allowed;
		document.getElementById("USER").disabled = allowed;
		document.getElementById("RESPONSABLE").disabled = allowed;
	});
}
function ajaxSearch(element, contextPath) {
	id = element.value;
	url = contextPath + "/jquery/hostCheck.do";
	$.ajax({
		type : "POST",
		url : url,
		data : "id=" + id,
		success : function(allowed) {
			document.getElementById("MANAGER").disabled = !allowed;
			document.getElementById("TECHNICIAN").disabled = !allowed;
			document.getElementById("USER").disabled = allowed;
			document.getElementById("RESPONSABLE").disabled = allowed;
			if (allowed) {
				document.getElementById("authority").value = document
						.getElementById("TECHNICIAN").value;
			} else {
				document.getElementById("authority").value = document
						.getElementById("USER").value;
			}
		},
		error : function(e) {
			alert(url);

		}
	});
}



function toggleDisabled(element) {
	document.getElementById(element).disabled=!document.getElementById(element).disabled;
}

function toggleVisible(element) {	
	var x = document.getElementById(element);
    if (x.style.display === 'none') {
        x.style.display = 'block';
    } else {
        x.style.display = 'none';
    }
}

// Toggle between showing and hiding the sidebar, and add overlay effect
function w3_open() {
	if (mySidebar.style.display === 'block') {
		mySidebar.style.display = 'none';
		overlayBg.style.display = "none";
	} else {
		mySidebar.style.display = 'block';
		overlayBg.style.display = "block";
	}
}

// Close the sidebar with the close button
function w3_close() {
	mySidebar.style.display = "none";
	overlayBg.style.display = "none";
}

// Script for Tabs, Accordions, Progress bars and slideshows -->

// Tabs
function openCity(evt, cityName) {
	var i;
	var x = document.getElementsByClassName("city");
	for (i = 0; i < x.length; i++) {
		x[i].style.display = "none";
	}
	var activebtn = document.getElementsByClassName("testbtn");
	for (i = 0; i < x.length; i++) {
		activebtn[i].className = activebtn[i].className.replace(
				" w3-dark-grey", "");
	}
	document.getElementById(cityName).style.display = "block";
	evt.currentTarget.className += " w3-dark-grey";
}
// Accordions
function myAccFunc(id) {
	var x = document.getElementById(id);
	if (x.className.indexOf("w3-show") == -1) {
		x.className += " w3-show";
	} else {
		x.className = x.className.replace(" w3-show", "");
	}
}
function plusDivs(n) {
	slideIndex = slideIndex + n;
	showDivs(slideIndex);
}

function showDivs(n) {
	var x = document.getElementsByClassName("mySlides");
	if (n > x.length) {
		slideIndex = 1
	}
	if (n < 1) {
		slideIndex = x.length
	}
	;
	for (i = 0; i < x.length; i++) {
		x[i].style.display = "none";
	}
	x[slideIndex - 1].style.display = "block";
}

// Progress Bars
function move() {
	var elem = document.getElementById("myBar");
	var width = 5;
	var id = setInterval(frame, 10);
	function frame() {
		if (width == 100) {
			clearInterval(id);
		} else {
			width++;
			elem.style.width = width + '%';
			elem.innerHTML = width * 1 + '%';
		}
	}
}

function askSubmission(msg, form) {
	if (confirm(msg))
		form.submit();
}
function relativeRedir(loc) {
	var b = document.getElementsByTagName('base');
	if (b && b[0] && b[0].href) {
		if (b[0].href.substr(b[0].href.length - 1) == '/'
				&& loc.charAt(0) == '/')
			loc = loc.substr(1);
		loc = b[0].href + loc;
	}
	window.location.replace(loc);
}

function overEffect(elemento) {
	elemento.classList.toggle("w3-text-white");
}

function setTexTColor(elemento, color) {
	elemento.classList.toggle("w3-text-white");
}

function ellipsis(elemento) {
	elemento.classList.toggle("ellipsis");
}

/*
 * When the user clicks on the button, toggle between hiding and showing the
 * dropdown content
 */
function myFunction() {
	document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
	if (!event.target.matches('.dropbtn')) {

		var dropdowns = document.getElementsByClassName("dropdown-content");
		var i;
		for (i = 0; i < dropdowns.length; i++) {
			var openDropdown = dropdowns[i];
			if (openDropdown.classList.contains('show')) {
				openDropdown.classList.remove('show');
			}
		}
	}
}

function plusSlides(n) {
	showSlides(slideIndex += n);
}

function currentSlide(n) {
	showSlides(slideIndex = n);
}

function showSlides(n) {
	var i;
	slideIndex = n;
	var slides = document.getElementsByClassName("mySlides");
	var dots = document.getElementsByClassName("dot");
	if (n > slides.length) {
		slideIndex = 1
	}
	if (n < 1) {
		slideIndex = slides.length
	}
	for (i = 0; i < slides.length; i++) {
		slides[i].style.display = "none";
	}
	for (i = 0; i < dots.length; i++) {
		dots[i].className = dots[i].className.replace(" active", "");
	}
	slides[slideIndex - 1].style.display = "block";
}

/* Drag and Drop */

function drop(evt, target) {
	evt.stopPropagation();
	evt.preventDefault();
	var imageUrl = evt.dataTransfer.getData('text/html');
	var url = $(imageUrl).attr("src");
	var elemento = ('<div class="mySlides fade"><img src="' + url + '" style="width: 100%"></div>');
	var imagen = '<img src="' + url + '" class="tableImg">';
	if (target == 'newspaper') {
		document.getElementById("fotosPath").innerHTML = url;
		$(document.getElementById("carrusel")).append(elemento);
		$(document.getElementById("fotos")).append(imagen);
	} else {
		if (document.getElementById("fotosPath").innerHTML.length > 0) {
			if (!document.getElementById("fotosPath").innerHTML.includes(url)) {
				document.getElementById("fotosPath").innerHTML = document
						.getElementById("fotosPath").innerHTML.concat(", "
						+ url);
				$(document.getElementById("carrusel")).append(elemento);
				$(document.getElementById("fotos")).append(imagen);
				var slides = document.getElementsByClassName("mySlides");
				var punto = ('<span class="dot" onclick="currentSlide('
						+ slides.length + ')"></span>');
				$(document.getElementById("punto")).append(punto);

			}
		} else {
			document.getElementById("fotosPath").innerHTML = document
					.getElementById("fotosPath").innerHTML.concat(url);
			$(document.getElementById("carrusel")).append(elemento);
			$(document.getElementById("fotos")).append(imagen);
			var slides = document.getElementsByClassName("mySlides");
			var punto = ('<span class="dot" onclick="currentSlide('
					+ slides.length + ')"></span>');
			$(document.getElementById("punto")).append(punto);

		}
	}
	showSlides(-1);
}

function checkPassword() {

	if (document.getElementById('password').value.length > 4
			&& document.getElementById('password').value.length < 33) {
		document.getElementById('password').style.color = 'green';
	} else {
		document.getElementById('password').style.color = 'red';
	}

	if (document.getElementById('password').value == document
			.getElementById('confirm_password').value) {
		document.getElementById('confirm_password').style.color = 'green';
	} else {
		document.getElementById('confirm_password').style.color = 'red';
	}
}

function checkEdition() {
	var enabled = false;
	var newPassword = document.getElementById('new_password');
	var confirmPassword = document.getElementById('confirm_password');

	if (document.getElementById('password').value.length != 0
			&& newPassword.value.length > 4 && newPassword.value.length < 33) {
		newPassword.style.color = 'green';
		if (newPassword.value == confirmPassword.value) {
			confirmPassword.style.color = 'green';
			enabled = true;
		} else
			confirmPassword.style.color = 'red';
	} else
		newPassword.style.color = 'red';

}

function showUserAccount() {

	var changedPassword = document.getElementById("changePassword");
	if (changedPassword.style.display == "block") {
		changedPassword.style.display = "none";
		document.getElementById("save").className = "formButton toLeft";
	} else {
		changedPassword.style.display = "block"
		checkEdition();
	}
}

function showMsg() {
	alert("alert!");

}

function showConfirmationAlert(element, msg, url) {
	var fullMsg = element + " " + msg;
	if (confirm(fullMsg)) {
		relativeRedir(url);
	}
}

function showConditionalAlert(msg, id, url) {

	if (id != '0') {

		relativeRedir(url);
	} else {
		alert(msg);
	}

}
