<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Term</title>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&libraries=places"></script>
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css" integrity="sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r" crossorigin="anonymous">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js" integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS" crossorigin="anonymous"></script>
<link rel="stylesheet" href="customTable.css"/>

<script type="text/javascript">
 
 function initialize() {

	 var input = document.getElementById('searchid');
	 var autocomplete = new google.maps.places.Autocomplete(input);
	 }

	 google.maps.event.addDomListener(window, 'load', initialize);
 
 </script>
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
			<li><a href="ANDController">Novelty Feature (And Boolean Query)</a></li>
			<li><a href="ORController">Novelty Feature (OR Boolean Query)</a></li>
			<li><a href="PdfSearch">Pdf Search</a></li>
		</ul>
	</div>
	</nav>

	<div class="container">
		<div class="col-md-6 col-md-offset-3">
			<form class="form-signin" action="SearchController" method="post">
				<h1 class="form-signin-heading">Search For a Term</h1>		
					
				<input type="text" id="searchid" class="form-control" placeholder="Enter a Search Term" name="searchTerm" required="" autofocus="">
				<br /> 
				<button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
			</form>
			<br />
		</div>
		
		<div class="col-md-3 col-md-offset-1" style="width:950px">
		<c:choose>
		<c:when test="${results==null}">
		
		
		</c:when>
		<c:otherwise>
		<table class="table table-striped table-hover table-responsive active"  style="padding-top: 10px;">
			<thead>
				<tr>
					<th>Counter</th>
					<th>Link Analysis Score</th>
					<th>TF-IDF Score</th>
					<th>Combined Score</th>
					<th>Document Name</th>
				</tr>
			<thead>
			<tbody>
			<c:forEach items="${results}" var="result">
				<tr>
					<td>${result.id}</td>
					<td>${result.score}</td>
					<td>${result.tfidf }</td>
					<td>${result.score + result.tfidf }</td>
					<td><a href="${result.filePath}">${result.fileName}</a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		
		</c:otherwise>
		
		</c:choose>
		
		 </div>
	</div>
</body>
</html>