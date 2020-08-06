var lat = 37.98;
var lon = 23.72;
var zoom = 15;
var marker = null;
var city;
var ip;

//function to reset view back to current point
function resetView(){
    map.setView([lat, lon], zoom);
    return;
};

//function to set marker
function setMarker(){
  if(marker == null){
    marker = new L.marker([lat, lon]).addTo(map);
  } else {
    marker.setLatLng([lat, lon]);
  }
  marker.bindPopup("<b>" + ip + "</b>" + "<br>" + lat.toString() + " , " + lon.toString() + "<br>" + city).openPopup();
  resetView();
};

// Restricts input for the given textbox to the given inputFilter function.
function setInputFilter(textbox, inputFilter) {
    ["input", "keydown", "keyup", "mousedown", "mouseup", "select", "contextmenu", "drop"].forEach(function(event) {
      textbox.addEventListener(event, function() {
        if (inputFilter(this.value)) {
          this.oldValue = this.value;
          this.oldSelectionStart = this.selectionStart;
          this.oldSelectionEnd = this.selectionEnd;
        } else if (this.hasOwnProperty("oldValue")) {
          this.value = this.oldValue;
          this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
        } else {
          this.value = "";
        }
      });
    });
};

//update weather values
function weatherUpdate(weather, icon, temp, pres, hum, speed, direction){
  document.getElementById("weather_info").innerHTML = weather;
  document.getElementById("weather_icon").src = "http://openweathermap.org/img/wn/"+icon+"@2x.png";
  var final_temp = temp - 273.15;
  final_temp = final_temp.toFixed(0);
  document.getElementById("temperature").innerHTML = final_temp + " &#8451;"; //from kelvin to celcius
  if(final_temp < 15){
    document.getElementById("temperature_icon").src = "./imgs/low_temp.png";
  } else if(final_temp > 30){
    document.getElementById("temperature_icon").src = "./imgs/high_temp.png";
  } else{
    document.getElementById("temperature_icon").src = "./imgs/mid_temp.png";
  }
  document.getElementById("pressure").innerHTML = pres + " kPa";
  document.getElementById("humidity").innerHTML = hum + " %";
  speed = speed / 3.6;
  speed = speed.toFixed(1);
  document.getElementById("wind_speed").innerHTML = speed + " km/h";
  setWindDir(direction);
}

//set wind direction
function setWindDir(d){
  document.getElementById("wind_icon").style.transform = "rotate("+d+"deg)"
}

//display warning function
function warning(msg){
  document.getElementById("warning_msg").innerHTML = msg;
}

//disable button
function disableButton(){
  var elem = document.getElementById("ip_button");
  elem.disabled = true;
  elem.innerHTML = "Loading...";
  document.getElementById("loading").className = "lds-ellipsis";
}

//enable button
function enableButton(){
  var elem = document.getElementById("ip_button");
  elem.disabled = false;
  elem.innerHTML = "Submit";
  document.getElementById("loading").className = "";
}

//fill your ip
function defaultIPShow(ip){
  var arr = ip.split(".");
  document.getElementById("ip1").value = arr[0];
  document.getElementById("ip2").value = arr[1];
  document.getElementById("ip3").value = arr[2];
  document.getElementById("ip4").value = arr[3];
}

//check input ip
function validateInput(in1, in2, in3, in4){
  if(in1 == "" && in2 == "" && in3 == "" && in4 == ""){
    return true;
  }
  if(in1 < 0 || in1 > 255 || in1 == ""){
    return false;
  }
  if(in2 < 0 || in2 > 255 || in2 == ""){
    return false;
  }
  if(in3 < 0 || in3 > 255 || in3 == ""){
    return false;
  }
  if(in4 < 0 || in4 > 255 || in4 == ""){
    return false;
  }
  return true;
}

//function to get host ip
function getMyIP(){
  var xhttp = new XMLHttpRequest();
  xhttp.timeout = 2000;
  xhttp.ontimeout = function(){
    warning("Please input an ip manually (ip discovery down)");
    enableButton();
  };
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 ) {
      if(this.status == 200){
        var myObj = JSON.parse(this.responseText);
        //set my ip and retry
        defaultIPShow(myObj.ip);
        submitIP();
      } else {
        //somthing went wronge with this web service
        warning("Please input an ip manually (ip discovery down)");
        enableButton();
      }
    }
  };
  xhttp.open("GET", "http://api.ipify.org/?format=json", true);
  xhttp.send();
}

//function to fetch weather data
function getWeatherInfo(ip){
  var xhttp, xmlDoc, weather, icon, temp, pres, hum, speed, dir, url;
  url = "http://127.0.0.1:8080/ip2weather?ip="+ip+"&mediaType=xml";
  xhttp = new XMLHttpRequest();
  xhttp.timeout = 4000;
  xhttp.ontimeout = function(){
    warning("Server is down, try again later");
    enableButton();
  };
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 ) {
      if(this.status == 200){
        //on successfull fetch get response values
        xmlDoc = this.responseXML;
        city = xmlDoc.getElementsByTagName("city")[0].childNodes[0].nodeValue;
        lat = xmlDoc.getElementsByTagName("lat")[0].childNodes[0].nodeValue;
        lon = xmlDoc.getElementsByTagName("lon")[0].childNodes[0].nodeValue;
        city = xmlDoc.getElementsByTagName("city")[0].childNodes[0].nodeValue;
        weather = xmlDoc.getElementsByTagName("weather")[0].childNodes[0].nodeValue;
        icon = xmlDoc.getElementsByTagName("icon")[0].childNodes[0].nodeValue;
        temp = xmlDoc.getElementsByTagName("temp")[0].childNodes[0].nodeValue;
        pres = xmlDoc.getElementsByTagName("pressure")[0].childNodes[0].nodeValue;
        hum = xmlDoc.getElementsByTagName("humidity")[0].childNodes[0].nodeValue;
        speed = xmlDoc.getElementsByTagName("windSpeed")[0].childNodes[0].nodeValue;
        dir = xmlDoc.getElementsByTagName("windDeg")[0].childNodes[0].nodeValue;
        weatherUpdate(weather, icon, temp, pres, hum, speed, dir);
        setMarker();
        enableButton();
      } else{
        //somthing went wronge with this web service?????????
        warning(this.responseXML.getElementsByTagName("message")[0].childNodes[0].nodeValue);
        enableButton();
      }
    }
  };
  xhttp.open("GET", url, true);
  xhttp.send();
}

//submit form called by submit button
function submitIP(){
  disableButton();
  ip1 = document.getElementById("ip1").value;
  ip2 = document.getElementById("ip2").value;
  ip3 = document.getElementById("ip3").value;
  ip4 = document.getElementById("ip4").value;
  if(!validateInput(ip1, ip2, ip3, ip4)){
    warning("Please enter a valid IP address");
    return;
  }
  //validation passed successfully -> disable button until response
  warning("");
  // ip1 == "" means that all fields where empty (validateInput())
  if(ip1 == ""){
    getMyIP();
    return;
  } else{
    ip = ip1.toString()+"."+ip2.toString()+"."+ip3.toString()+"."+ip4.toString();
  }
  getWeatherInfo(ip);
}