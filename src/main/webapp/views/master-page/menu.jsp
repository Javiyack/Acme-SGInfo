<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<security:authorize access="isAuthenticated()">
    <jstl:set var="colom" value=", "/>
    <security:authentication property="principal.username" var="username"/>
    <security:authentication property="principal.authorities[0]"
                             var="permiso"/>
    <jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
</security:authorize>


<jstl:if test="${rol == 'user' or rol == 'responsible'}">
    <jstl:set value="external" var="accesscontrol"/>
</jstl:if>
<jstl:if test="${rol == 'technician' or rol == 'manager' or rol == 'administrator'}">
    <jstl:set value="internal" var="accesscontrol"/>
</jstl:if>

<!-- Menu and banner usually + "$") -->
<!-- Sidebar/menu -->

<nav
        class="w3-sidebar w3-collapse w3-flat-midnight-blue w33-animate-left sombra"
        style="z-index: 3; width: 260px;" id="mySidebar">

    <br>

    <div class="w3-container w3-row">
        <div class="w3-col s4">
            <security:authorize access="isAuthenticated()">
                <a href="j_spring_security_logout" class=""><i
                        class="fa fa-sign-out w3-margin w3-xxlarge"></i></a>
            </security:authorize>
            <security:authorize access="isAnonymous()">
                <a href="security/login.do" class=""><i
                        class="fa fa-sign-in w3-margin w3-xxlarge"></i></a>
            </security:authorize>
        </div>
        <div class="w3-col s8 w3-bar">
            <span><spring:message code="welcome.greeting.msg"/>${colom}<strong>${username}</strong></span><br>
            <security:authorize access="isAnonymous()">
                <a href="actor/create.do" class=""><i
                        class="fa fa-user-plus w3-bar-item w3-large"></i></a>
            </security:authorize>
            <a href="folder/list.do"><i class="fa fa-envelope w3-bar-item w3-large"></i></a>
            <security:authorize access="isAuthenticated()">
                <a href="actor/edit.do"><i
                        class="fa fa-user w3-bar-item w3-large"></i></a>
                <a href="customer/displayOwn.do"><i class="fa fa-cog w3-bar-item w3-large"></i></a>
            </security:authorize>
        </div>
    </div>
    <hr>
    <div class="w3-bar-block" style="padding-bottom: 60px">
        <a href="servant/list.do"
           class="w3-bar-item w3-button w3-padding w3-xlarge" id="servant"><i
                class="fas fa-shield-alt fa-fw w3-margin-right"></i><spring:message
                code="label.services"/>
        </a>
        <security:authorize access="isAnonymous()">
            <a href="customer/list.do"
               class="w3-bar-item w3-button w3-padding w3-xlarge" id="customer"><i
                    class="fa fa-diamond fa-fw w3-margin-right"></i><spring:message
                    code="label.customers"/>
            </a>
        </security:authorize>
        <security:authorize access="hasAnyRole('USER', 'RESPONSIBLE')">
            <a href="customer/list.do"
               class="w3-bar-item w3-button w3-padding w3-xlarge"  id="customer"><i
                    class="fa fa-diamond fa-fw w3-margin-right"></i><spring:message
                    code="label.customers"/>
            </a>
            <a href="actor/responsible/list.do" name="menuItem"
               class="w3-bar-item w3-button w3-padding w3-xlarge" id="actor"><i
                    class="fa fa-users fa-fw w3-margin-right w3-margin-right"></i><spring:message code="label.users"/>
            </a>
        </security:authorize>
        <security:authorize access="hasRole('USER')">

        </security:authorize>
        <security:authorize access="hasRole('TECHNICIAN')">
        <a href="customer/internal/list.do"
           class="w3-bar-item w3-button w3-padding w3-xlarge" id="customer"><i
                class="fa fa-diamond fa-fw w3-margin-right"></i><spring:message
                code="label.customers"/>
        </security:authorize>
        <security:authorize access="hasRole('MANAGER')">
            <a href="customer/internal/list.do"
               class="w3-bar-item w3-button w3-padding w3-xlarge" id="customer"><i
                    class="fa fa-diamond fa-fw w3-margin-right"></i><spring:message
                    code="label.customers"/>
            </a>
            <a href="request/manager/list.do" class="w3-bar-item w3-button w3-padding w3-xlarge" id="request"><i
                    class="fas fa-tasks fa-fw w3-margin-right"></i><spring:message
                    code="label.requests"/>
            </a>

            <a id="bill" class="w3-bar-item w3-button w3-padding w3-xlarge" onclick="myAccordionFunc('billingAcc')"
               name="menuItem">
                <i class="fa fa fa-bank fa-fw w3-margin-right"></i><spring:message code="label.billing"/>
            </a>
            <div id="billingAcc" class="w3-hide w3-card sombra" name="accordion">

                <a href="billing/manager/labor/list.do"
                   class="w3-bar-item w3-button w3-padding w3-large w3-light-gray"
                   style="padding-left: 2em !important;"><i
                        class="far fa-money-bill-alt fa-fw w3-margin-right"></i><spring:message code="label.incidence"/>
                </a>
                <a href="billing/manager/service/list.do"
                   class="w3-bar-item w3-button w3-padding w3-large w3-light-gray"
                   style="padding-left: 2em !important;"><i
                        class="fas fa-money-bill-alt fa-fw w3-margin-right"></i><spring:message code="label.service"/>
                </a>
            </div>

            <a href="actor/manager/list.do" name="menuItem"
               class="w3-bar-item w3-button w3-padding w3-xlarge" id="actor"><i
                    class="fa fa-users fa-fw w3-margin-right"></i><spring:message code="label.users"/>
            </a>

        </security:authorize>
        <security:authorize access="isAuthenticated()">
            <a href="incidence/${accesscontrol}/list.do"
               class="w3-bar-item w3-button w3-padding w3-xlarge"  id="incidence"><i
                    class="fa fa-ambulance fa-fw w3-margin-right"></i><spring:message
                    code="label.incidences"/>
            </a>
        </security:authorize>


        <security:authorize access="hasRole('RESPONSIBLE')">
            <a href="request/responsible/list.do" class="w3-bar-item w3-button w3-padding w3-xlarge" id="request"><i
                    class="fas fa-tasks fa-fw w3-margin-right"></i><spring:message
                    code="label.requests"/>
            </a>
            <a id="bill" class="w3-bar-item w3-button w3-padding w3-xlarge" onclick="myAccordionFunc('billingAcc')"
               name="menuItem">
                <i class="fa fa fa-bank fa-fw w3-margin-right"></i><spring:message code="label.billing"/>
            </a>
            <div id="billingAcc" class="w3-hide w3-card sombra" name="accordion">

                <a href="billing/responsible/labor/list.do"
                   class="w3-bar-item w3-button w3-padding w3-large w3-light-gray"
                   style="padding-left: 2em !important;"><i
                        class="far fa-money-bill-alt fa-fw w3-margin-right"></i><spring:message code="label.incidence"/>
                </a>
                <a href="billing/responsible/service/list.do"
                   class="w3-bar-item w3-button w3-padding w3-large w3-light-gray"
                   style="padding-left: 2em !important;"><i
                        class="fas fa-money-bill-alt fa-fw w3-margin-right"></i><spring:message code="label.service"/>
                </a>
            </div>

        </security:authorize>

        <security:authorize access="hasRole('ADMINISTRATOR')">

            <a href="customer/administrator/list.do"
               class="w3-bar-item w3-button w3-padding w3-xlarge" id="customer"><i
                    class="fa fa-diamond fa-fw w3-margin-right"></i><spring:message
                    code="label.customers"/>
            </a><a href="actor/administrator/list.do" name="menuItem"
               class="w3-bar-item w3-button w3-padding w3-xlarge" id="actor"><i
                    class="fa fa-users fa-fw w3-margin-right w3-margin-right"></i><spring:message code="label.users"/>
            </a>
            <a href="administrator/dashboard.do" id="dashboard"
               class="w3-bar-item w3-button w3-padding w3-xlarge" name="menuItem"><i
                    class="fa fa-dashboard fa-fw w3-margin-right"></i><spring:message
                    code="master.page.dashboard"/>
            </a>
            <a href="configuration/administrator/edit.do"
               class="w3-bar-item w3-button w3-padding w3-xlarge" id="configuration">
                <i class="fa fa-cog fa-fw w3-margin-right"></i>Settings </a>
        </security:authorize>

        <security:authorize access="isAnonymous()">

        </security:authorize>
        <br>
        <br>
    </div>

</nav>


<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity"
     onclick="w3_close()" style="cursor: pointer" title="close side menu"
     id="myOverlay"></div>
<script>
    var mySidebar = document.getElementById("mySidebar");

    //Get the DIV with overlay effect
    var overlayBg = document.getElementById("myOverlay");

    //Toggle between showing and hiding the sidebar, and add overlay effect
    function w3_open() {
        if (mySidebar.style.display === 'block') {
            mySidebar.style.display = 'none';
            overlayBg.style.display = "none";
        } else {
            mySidebar.style.display = 'block';
            overlayBg.style.display = "block";
        }
    }

    //Close the sidebar with the close button
    function w3_close() {
        mySidebar.style.display = "none";
        overlayBg.style.display = "none";
    }

    var btnContainer = document.getElementById("myDIV");

    //Get all buttons with class="btn" inside the container
    var btns = btnContainer.getElementsByClassName("btn");

    //Loop through the buttons and add the active class to the current/clicked button
    for (var i = 0; i < btns.length; i++) {
        btns[i].addEventListener("click", function () {
            var current = document.getElementsByClassName("active");
            current[0].className = current[0].className.replace(" active", "");
            this.className += " active";
        });
    }


    // Get the modal
    var modal = document.getElementById('myModal');

    // Get the button that opens the modal
    var btn = document.getElementById("myBtn");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks the button, open the modal
    btn.onclick = function () {
        modal.style.display = "block";
    };

    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    };

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    };

</script>