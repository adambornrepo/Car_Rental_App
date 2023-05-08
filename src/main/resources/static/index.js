
    function loadTable() {
    const xhttp = new XMLHttpRequest();
    xhttp.open("GET","http://localhost:8080/car");
    xhttp.send();
    xhttp.onreadystatechange=function(){
    if(this.readyState == 4&&this.status==200){
    console.log(this.responseText);
    var trHTML='';
    const objects=JSON.parse(this.responseText);
    for(let object of objects){
        trHTML+='<div class="row">';
        trHTML += '<div class="col-md-3">';
        trHTML += '<img src="images/1.jpg" alt="" height="100px" class="w-100 img-fluid rounded-4">' + '</div>';
        trHTML += '<div class="col-md-3">';
        trHTML += '<p class="m-1"><b>Model Yılı: </b>' + object['productYear'] + '</p>';
        trHTML += '<p class="m-1"><b>Yakıt Tipi: </b>' + object['fuelType'] + '</p>';
        trHTML += '<p class="m-1"><b>Araç Kilometresi: </b>' + object['mileage'] + '</p>';
        trHTML += '</div>';
        trHTML += '<div class="col-md-3">';
        trHTML += '<p class="m-1"><b>Araç Tipi: </b>' + object['carType'] + '</p>';
        trHTML += '<p class="m-1"><b>Araç Rengi: </b>' + object['color'] + '</p>';
        trHTML += '<p class="m-1"><b>Koltuk Sayısı: </b>' + object['seatCount'] + '</p>';
        trHTML += '</div>';
        trHTML += '<div class="col-md-3 text-center">';
        trHTML += '<h2>' + object['brand'] + '</h2>';
        trHTML += '<p class="m-1"><b>Plaka Numarası:</b>' + object['plateNumber'] + '</p>';
        trHTML += '<p class="m-0"><b>Ücret:</b>' + object['dailyPrice'] + '/Gün <sub></sub></p>';
        trHTML += '</div>';
        trHTML+='</div>';
    }
    document.getElementById("mydiv").innerHTML=trHTML;
}
};
}

    function deleteCar(plateNumber){
    console.log(plateNumber)
    const xhttp = new XMLHttpRequest();
    xhttp.open("DELETE","http://localhost:8080/car/" + plateNumber);
    xhttp.setRequestHeader("Content-Type","application/json;charset=UTF-8");
    xhttp.send(null);
    xhttp.onreadystatechange = function(){
    if(this.readyState==4){
    const objects = JSON.parse(this.responseText);
    Swal.fire(objects['message']);
    loadTable();
}
};
}