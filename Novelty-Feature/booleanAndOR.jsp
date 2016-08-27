<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Boolean Operation (AND)</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
	<link rel="stylesheet" href="customTable.css"/>
</head>
<body>
	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">CS 454 - Search Engine</a>
		</div>
		<ul class="nav navbar-nav">
			<li><a href="HomeController">Home Page</a></li>
			<li class="active"><a href="SearchController">Relational ranking</a></li>
		</ul>
	</div>
	</nav>
	
	<div class="container">
		<div class="col-md-6">
			<form class="form-signin" action="ANDController" method="post">
				<h1 class="form-signin-heading">Boolean AND</h1>		
					
				<input type="text" class="form-control" placeholder="First Search Term" name="searchTerm" required="" autofocus="">
				&nbsp;&nbsp; AND &nbsp;&nbsp;
				<input type="text" class="form-control" placeholder="Second Search Term" name="searchTerm2" required="" autofocus="">
				 <br />
				<button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
			</form>
			<br />
		</div>
		
		<div class="col-md-6">
			<form class="form-signin" action="ORController" method="post">
				<h1 class="form-signin-heading">Boolean OR</h1>		
					
				<input type="text" class="form-control" placeholder="First Search Term" name="searchTerm" required="" autofocus="">
				&nbsp;&nbsp; OR &nbsp;&nbsp;
				<input type="text" class="form-control" placeholder="Second Search Term" name="searchTerm2" required="" autofocus="">
				 <br />
				<button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
			</form>
			<br />
		</div>
		
		<div class="col-md-3 col-md-offset-2">
		 <table class="table table-striped table-hover table-responsive active"  style="padding-top: 10px;">
			<thead>
				<tr>
					<th>Counter</th>
					<th>TF-IDF</th>
					<th>Document Name</th>
				</tr>
			<thead>
			<tbody>
			<c:forEach items="${results}" var="result">
				<tr>
					<td>${result.id}</td>
					<td>${result.tfidf }</td>
					<td><a href="${result.filePath}">${result.fileName}</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		</div>
	</div>
	
</body>
</html>