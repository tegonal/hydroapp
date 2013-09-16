$(document).ready ->
  updateStations($("#searchField").val())
  
  $("#station-container").hide()
  $("#chart-container").hide()
  
updateStations = (filter = '') ->
  jsRoutes.controllers.Stations.list(filter).ajax
    success: (data) ->
      $("#stations").html data
      registerAjaxLinks()
    error: (err) ->

toggleFavs = (element) ->
	if element.hasClass 'fav-off'
		element.removeClass 'fav-off'
		element.addClass 'fav-on'
		element.parent().animate({
			opacity: 0,
			position: 'absolute',
			top:-200}, 1500 )
		setTimeout(->
			updateStations($("#searchField").val())
		,1000);
	else 
		element.removeClass 'fav-on'
		element.addClass 'fav-off'
		element.parent().animate({
			opacity: 0,
			position: 'absolute',
			top:200}, 1500 )
		setTimeout(->
			updateStations($("#searchField").val())
		,1000);
		

registerAjaxLinks = () ->
  $(".fav").click (event) ->
    event.preventDefault()
    $.get @href, {}, (data) ->
    	$(event.delegateTarget).toggleClass(toggleFavs($(event.delegateTarget)))

  $(".station-link").click (event) ->
    event.preventDefault()
    $.get @href, {}, (data) ->
      $("#station-container").html data
      $("#station-container").hide().fadeIn(200)
      $("#chart-container").hide()
      registerChartAjaxLinks()
  
 registerChartAjaxLinks = () ->    
  $(".chart-link").click (event) ->
    event.preventDefault()
    $.get @href, {}, (jsonData) ->
        data = new google.visualization.DataTable(jsonData)

        chart = new google.visualization.AreaChart(document.getElementById('chart-container'))
        $("#chart-container").hide().fadeIn(200)
        chart.draw(data, jsonData["options"])
  
$ ->
  $("#searchField").keyup ->
    updateStations($(this).val())

  $("#searchField").click ->
    updateStations($(this).val())
