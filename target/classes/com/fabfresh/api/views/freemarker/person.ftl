<#-- @ftlvariable name="" type="com.fabfresh.api.views.PersonView" -->
<html>
    <body>
        <!-- calls getPerson().getEmail() and sanitizes it -->
        <h1>Hello, ${person.email?html}!</h1>
    </body>
</html>
