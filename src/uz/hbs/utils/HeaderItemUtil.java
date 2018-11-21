package uz.hbs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.StringResourceModel;

import uz.hbs.MyWebApplication;

public class HeaderItemUtil {
	public static void setDatetimepickerJavaScriptContentHeaderItem(IHeaderResponse response, Locale locale) {
		setDatetimepickerJavaScriptContentHeaderItem(response, locale, null, MyWebApplication.DATE_FORMAT);
	}

	public static void setDatetimepickerJavaScriptContentHeaderItem(IHeaderResponse response, Locale locale, Date defaultValue) {
		setDatetimepickerJavaScriptContentHeaderItem(response, locale, defaultValue, MyWebApplication.DATE_FORMAT);
	}

	public static void setDatetimepickerJavaScriptContentHeaderItem(IHeaderResponse response, Locale locale, Date defaultValue,
			String datePatternFormat) {
		String defaultValueStr = "";
		if (defaultValue != null) {
			defaultValueStr = new SimpleDateFormat(datePatternFormat).format(defaultValue);
		}

		response.render(JavaScriptHeaderItem.forScript(
				"$(document).ready(function(){$('.input-group.date').datetimepicker({pickDate:true,pickTime:false,showToday:true,language:'"
						+ locale.getLanguage() + "',defaultDate:'" + defaultValueStr + "',format:'"+datePatternFormat.toUpperCase()+"'});});", "datetimepickerid1"));
	}

	public static void setGoogleMapUrlOnlyHeaderItem(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forUrl("https://maps.googleapis.com/maps/api/js?key="
				+ MyWebApplication.getConfigBundle().getString("google_map_api_key")));
	}

	public static void setGoogleMapHeaderItem(IHeaderResponse response, boolean showAddress) {
		String googleApiKey = MyWebApplication.getConfigBundle().getString("google_map_api_key");
		String zoom = MyWebApplication.getConfigBundle().getString("google_map_api_initial_zoom");
		String fixDot = MyWebApplication.getConfigBundle().getString("google_map_api_number_length_after_dot");
		String lngLatitude = new StringResourceModel("latitude", null).getString();
		String lngLongitude = new StringResourceModel("longitude", null).getString();
		String lngAddress = new StringResourceModel("hotels.details.address", null).getString();
		String latitude = MyWebApplication.getConfigBundle().getString("google_map_api_initial_latitude");
		String longitude = MyWebApplication.getConfigBundle().getString("google_map_api_initial_longitude");
		StringBuffer s = new StringBuffer("");
		s.append("function initialize() {");
		s.append("	var mapViewZoom = " + zoom + ";");
		s.append("	var fixDot = " + fixDot + ";");
		s.append("	var addressBlock = " + (showAddress ? "'<br>' + lang.address + ': ' + address" : "''") + ";");
		s.append("	var lang = {};");
		s.append("	lang.latitude = '" + lngLatitude + "';");
		s.append("	lang.longitude = '" + lngLongitude + "';");
		s.append("	lang.address = '" + lngAddress + "';");
		s.append("	var myCenter = new google.maps.LatLng(" + latitude + ", " + longitude + ");");
		s.append("	var infowindow = new google.maps.InfoWindow();");
		s.append("	var geocoder = new google.maps.Geocoder();");
		s.append("	var address = '';");
		s.append("	var mapOptions = {center: myCenter, zoom: mapViewZoom, mapTypeId: google.maps.MapTypeId.ROADMAP};");
		s.append("	var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);");
		s.append("	var marker = new google.maps.Marker({position: myCenter, draggable: true});");
		s.append("	google.maps.event.addListener(marker, 'dragend', function(event) {placeMarker(event.latLng);setMarkerCoordinates(event);});");
		s.append("	google.maps.event.addListener(map, 'click', function(event) {placeMarker(event.latLng);setMarkerCoordinates(event);});");
		s.append("	function placeMarker(location) {");
		s.append("		if (marker == undefined) {");
		s.append("			marker = new google.maps.Marker({");
		s.append("				position: location,");
		s.append("				map: map,");
		s.append("				animation : google.maps.Animation.DROP");
		s.append("			});");
		s.append("		} else {");
		s.append("			marker.setPosition(location);");
		s.append("		}");
		s.append("		if (infowindow) infowindow.close();");
		s.append("		geocoder.geocode({'latLng': location}, function(results, status) {");
		s.append("			if (status == google.maps.GeocoderStatus.OK) {");
		s.append("				if (results[0]) {");
		s.append("					address = results[0].formatted_address;");
		s.append("				}");
		s.append("			} else {");
		s.append("				address = '';");
		s.append("			}");
		s.append("		});");
		s.append("		infowindow.setContent(lang.latitude + ': ' + location.lat().toFixed(fixDot) + '<br>' + lang.longitude + ': ' + location.lng().toFixed(fixDot) + addressBlock);");
		s.append("		infowindow.open(map, marker);");
		s.append("	}");
		s.append("	marker.setMap(map);");
		s.append("	function setMarkerCoordinates(event) {");
		s.append("		document.getElementById('latitude').value = event.latLng.lat().toFixed(fixDot);");
		s.append("		document.getElementById('longitude').value = event.latLng.lng().toFixed(fixDot);");
		s.append("	}");
		s.append("}");
		s.append("google.maps.event.addDomListener(window, 'load', initialize);");

		response.render(JavaScriptHeaderItem.forUrl("https://maps.googleapis.com/maps/api/js?key=" + googleApiKey));
		response.render(JavaScriptHeaderItem.forScript(s, "googlemapsapi"));
	}

	public static void setGoogleMapHeaderReadOnlyItem(IHeaderResponse response, String mapCanvasId, boolean showAddress,
			double latitude, double longitude) {
		String googleApiKey = MyWebApplication.getConfigBundle().getString("google_map_api_key");
		String zoom = MyWebApplication.getConfigBundle().getString("google_map_api_initial_zoom");
		String fixDot = MyWebApplication.getConfigBundle().getString("google_map_api_number_length_after_dot");
		String lngLatitude = new StringResourceModel("latitude", null).getString();
		String lngLongitude = new StringResourceModel("longitude", null).getString();
		String lngAddress = new StringResourceModel("hotels.details.address", null).getString();
		StringBuffer s = new StringBuffer("");
		s.append("function initialize() {");
		s.append("	var mapViewZoom = " + zoom + ";");
		s.append("	var fixDot = " + fixDot + ";");
		s.append("	var addressBlock = " + (showAddress ? "'<br>' + lang.address + ': ' + address" : "''") + ";");
		s.append("	var lang = {};");
		s.append("	lang.latitude = '" + lngLatitude + "';");
		s.append("	lang.longitude = '" + lngLongitude + "';");
		s.append("	lang.address = '" + lngAddress + "';");
		s.append("	var myCenter = new google.maps.LatLng(" + latitude + ", " + longitude + ");");
		s.append("	var infowindow = new google.maps.InfoWindow();");
		s.append("	var geocoder = new google.maps.Geocoder();");
		s.append("	var address = '';");
		s.append("	var mapOptions = {center: myCenter, zoom: mapViewZoom, mapTypeId: google.maps.MapTypeId.ROADMAP};");
		s.append("	var map = new google.maps.Map(document.getElementById('" + mapCanvasId + "'), mapOptions);");
		s.append("	var marker = new google.maps.Marker({position: myCenter, draggable: false});");
		s.append("	function placeMarker(location) {");
		s.append("		if (marker == undefined) {");
		s.append("			marker = new google.maps.Marker({");
		s.append("				position: location,");
		s.append("				map: map,");
		s.append("				animation : google.maps.Animation.DROP");
		s.append("			});");
		s.append("		} else {");
		s.append("			marker.setPosition(location);");
		s.append("		}");
		s.append("		if (infowindow) infowindow.close();");
		s.append("		geocoder.geocode({'latLng': location}, function(results, status) {");
		s.append("			if (status == google.maps.GeocoderStatus.OK) {");
		s.append("				if (results[0]) {");
		s.append("					address = results[0].formatted_address;");
		s.append("				}");
		s.append("			} else {");
		s.append("				address = '';");
		s.append("			}");
		s.append("		});");
		s.append("		infowindow.setContent(lang.latitude + ': ' + location.lat().toFixed(fixDot) + '<br>' + lang.longitude + ': ' + location.lng().toFixed(fixDot) + addressBlock);");
		s.append("		infowindow.open(map, marker);");
		s.append("	}");
		s.append("	marker.setMap(map);");
		
		s.append("}");
		s.append("google.maps.event.addDomListener(window, 'load', initialize);");

		response.render(JavaScriptHeaderItem.forUrl("https://maps.googleapis.com/maps/api/js?key=" + googleApiKey));
		response.render(JavaScriptHeaderItem.forScript(s, "googlemapsapi2"));
	}
	
	public static void setGoogleMapHeaderItem(IHeaderResponse response, String mapCanvasId, boolean showAddress,
			double latitude, double longitude) {
		String googleApiKey = MyWebApplication.getConfigBundle().getString("google_map_api_key");
		String zoom = MyWebApplication.getConfigBundle().getString("google_map_api_initial_zoom");
		String fixDot = MyWebApplication.getConfigBundle().getString("google_map_api_number_length_after_dot");
		String lngLatitude = new StringResourceModel("latitude", null).getString();
		String lngLongitude = new StringResourceModel("longitude", null).getString();
		String lngAddress = new StringResourceModel("hotels.details.address", null).getString();
		StringBuffer s = new StringBuffer("");
		s.append("function initialize() {");
		s.append("	var mapViewZoom = " + zoom + ";");
		s.append("	var fixDot = " + fixDot + ";");
		s.append("	var addressBlock = " + (showAddress ? "'<br>' + lang.address + ': ' + address" : "''") + ";");
		s.append("	var lang = {};");
		s.append("	lang.latitude = '" + lngLatitude + "';");
		s.append("	lang.longitude = '" + lngLongitude + "';");
		s.append("	lang.address = '" + lngAddress + "';");
		s.append("	var myCenter = new google.maps.LatLng(" + latitude + ", " + longitude + ");");
		s.append("	var infowindow = new google.maps.InfoWindow();");
		s.append("	var geocoder = new google.maps.Geocoder();");
		s.append("	var address = '';");
		s.append("	var mapOptions = {center: myCenter, zoom: mapViewZoom, mapTypeId: google.maps.MapTypeId.ROADMAP};");
		s.append("	var map = new google.maps.Map(document.getElementById('" + mapCanvasId + "'), mapOptions);");
		s.append("	var marker = new google.maps.Marker({position: myCenter, draggable: true});");
		s.append("	google.maps.event.addListener(marker, 'dragend', function(event) {placeMarker(event.latLng);setMarkerCoordinates(event);});");
		s.append("	google.maps.event.addListener(map, 'click', function(event) {placeMarker(event.latLng);setMarkerCoordinates(event);});");
		s.append("	function placeMarker(location) {");
		s.append("		if (marker == undefined) {");
		s.append("			marker = new google.maps.Marker({");
		s.append("				position: location,");
		s.append("				map: map,");
		s.append("				animation : google.maps.Animation.DROP");
		s.append("			});");
		s.append("		} else {");
		s.append("			marker.setPosition(location);");
		s.append("		}");
		s.append("		if (infowindow) infowindow.close();");
		s.append("		geocoder.geocode({'latLng': location}, function(results, status) {");
		s.append("			if (status == google.maps.GeocoderStatus.OK) {");
		s.append("				if (results[0]) {");
		s.append("					address = results[0].formatted_address;");
		s.append("				}");
		s.append("			} else {");
		s.append("				address = '';");
		s.append("			}");
		s.append("		});");
		s.append("		infowindow.setContent(lang.latitude + ': ' + location.lat().toFixed(fixDot) + '<br>' + lang.longitude + ': ' + location.lng().toFixed(fixDot) + addressBlock);");
		s.append("		infowindow.open(map, marker);");
		s.append("	}");
		s.append("	marker.setMap(map);");
		s.append("	function setMarkerCoordinates(event) {");
		s.append("		document.getElementById('latitude').value = event.latLng.lat().toFixed(fixDot);");
		s.append("		document.getElementById('longitude').value = event.latLng.lng().toFixed(fixDot);");
		s.append("	}");
		s.append("}");
		s.append("google.maps.event.addDomListener(window, 'load', initialize);");
		
		response.render(JavaScriptHeaderItem.forUrl("https://maps.googleapis.com/maps/api/js?key=" + googleApiKey));
		response.render(JavaScriptHeaderItem.forScript(s, "googlemapsapi2"));
	}
}
