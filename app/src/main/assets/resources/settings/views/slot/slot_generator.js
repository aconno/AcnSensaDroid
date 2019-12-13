function generateUIDContent(namespaceId, instanceId) {
    return '<div class="row">'
        + '<div class="col-2">'
        + '<label>Namespace Id</label>'
        + '</div>'
        + '<div class="col-9">'
        + '<input type="text" style="width: 100%;" id="uid_namespace_id" value="' + namespaceId + '" class="holo">'
        + '</div>'
        + '</div>'
        + '<div class="row" style="margin-top: 1em;">'
        + ' <div class="col-3">'
        + ' <label>Instance Id</label>'
        + '</div>'
        + '<div class="col-9">'
        + '<input type="text" style="width: 100%;" id="uid_instance_id" value="' + instanceId + '" class="holo">'
        + '</div>'
        + '</div>';
}

function generateURLContent(url) {
    return '<div class="row">'
        + '<div class="col-3">'
        + '<label>URL</label>'
        + '</div>'
        + '<div class="col-9">'
        + '<input style="width: 100%;" type="text" id="url" value="' + url + '" class="holo">'
        + '</div>'
        + '</div>';
}

function generateIBeaconContent(uuid, major, minor) {
    return '<div class="row">'
        + '<div class="col-3">'
        + '<label>UUID</label>'
        + '</div>'
        + '<div class="col-9">'
        + '<input style="width: 100%;" type="text" id="ibeacon_uuid" value="' + uuid + '" class="holo">'
        + '</div>'
        + '</div>'
        + '<div class="row" style="margin-top: 1em;">'
        + '<div class="col-3">'
        + '<label>Major</label>'
        + '</div>'
        + '<div class="col-9">'
        + '<input style="width: 100%;" type="number" min="0" id="ibeacon_major" value="' + major + '" onkeypress="return onlyPositiveNumberFilter(event);" class="holo">'
        + '</div>'
        + '</div>'
        + '<div class="row" style="margin-top: 1em;">'
        + '<div class="col-3">'
        + '<label>Minor</label>'
        + '</div>'
        + '<div class="col-9">'
        + '<input style="width: 100%;" type="number" min="0" id="ibeacon_minor" value="' + minor + '" onkeypress="return onlyPositiveNumberFilter(event);" class="holo">'
        + '</div>'
        + '</div>';
}

function generateCustomContent(customValue, isHexModeOn) {
    return '<div class="row">'
        + '<div class="col-12">'
        + '<label>Custom</label>'
        + '</div>'
        + '</div>'
        + '<div class="row" style="margin-top: 1em;">'
        + '<div class="col-3">'
        + '<label>Custom Value</label>'
        + '</div>'
        + '<div class="col-9">'
        + '<input style="width: 100%;" type="text" id="custom_value" value="' + customValue + '" class="holo">'
        + '</div>'
        + '</div>'
        + '<div class="row" style="margin-top: 1em;">'
        + '<div class="col-3">'
        + '<label>Hex Mode</label>'
        + '</div>'
        + '<div align="right" class="col-9">'
        + '<div class="custom-control custom-switch">'
        + '<input id="custom_hex_enabled" ' + (isHexModeOn ? 'checked' : '') + ' class="custom-control-input" type="checkbox">'
        + '<label class="custom-control-label" for="custom_hex_enabled"></label>'
        + '</div>'
        + '</div>'
        + '</div>';
}

function generateDefaultContent(value) {

    return  '<div class="panel-body">'
              + '<div class="panel-body">'
              + '<input type="text" value='+value+' id="default_advertising_content"/>'
              + '</div>'
              + '</div>';
}

function generateBaseParameter(interval_checked) {
     return '<div class="row">'
            + '<div class="col-12">'
            + '<label>Base Parameter</label>'
            + '</div>'
            + '</div>';
}

function generateEnums(elements, id, name, value, index) {
        let body = generateEnumsBody(id, elements, index)
        let html =  '<div class="panel-body">';
        html += '<div class="form-group">';
        html += '<label for="ddl-lbl-' + id + '">' + name + '</label>';
        html += '<div id="ddl-' + id + '" class="dropdown">';
        html += '<button class="btn btn-secondary dropdown-toggle" type="button" id="ddl-menu-button-' + id + '"  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">';
        html += elements[value];
        html += "</button>";
        html += body;
        html += '</div>';
        html += '</div>';
        html += '</div>';
        return html;
    }

function generateEnumsBody(id, elements, index) {
        var html = '<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">';
        elements.forEach((element, position) => {
            html += generateSingleEnum(id, element, position, index);
        });
        html += '</div>';
        return html
    }

function generateSingleEnum(id, element, position, index) {
        return '<a onclick="dropDownChanged(' + id + ', \'' + element + '\', ' + position + ' ,' + index + ')" class="dropdown-item" >' + element + '</a>';
    }

function generateAdvertisingRange(name, advertisingInterval, value) {

    return  '<div class="panel-body">'
          + '<label>'+ name+' </label> </div>'
          + '<div class="panel-body">'
          +  '<label id="advertising_interval_time">'+ "Every"+ advertisingInterval+'</label> <br>'
          + '<input type="range" value='+value+' id="advertising_interval_time_range"/>'
          + '</div>'
          + '</div>';
}

function generateRange(name, value, id) {

    return  '<div class="panel-body">'
              + '<label>'+ name+' </label> </div>'
              + '<div class="panel-body">'
              + '<input type="range" value="'+value+'" id="range-' + id + '"/>'
              + '</div>'
              + '</div>';
}

function generateSwitchContent(isChecked, name) {
    let value;

    if(isChecked)
        value = "checked";
    else
        value = "";

    return '<div class="row">'
           + '<div class="col-12">'
           + '<div class="form-group">'
           + '<div class="custom-control custom-switch" >'
           + '<label class="custom-control-label" for="slot_advertising_switch">'+ name +' </label>'
           + '<input type="checkbox" class="custom-control-input"  data-toggle="toggle" id="slot_advertising_switch" name="" checked="'+ value+'"/>'
           + '</div>'
           + '</div>'
           + '</div>'
           + '</div>';
}

function generateAdvertisingIntervalInfo(interval) {
    return '<div class="row">'
        + '<div class="col-12">'
        + '<span id = "advertising-interval-info">' + timeToHighestOrder(interval)+ '</span>'
        + '</div>'
        + '</div>';
}

function generateFrameTypeMenuItem(name) {
    return '<li><a class="dropdown-item" id="frame_type_' + name.toLowerCase() + '" href="#">' + name + '</a></li>'
}

function onlyPositiveNumberFilter(event) {
    return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 0
}


function timeToHighestOrder(copy) {

    let millis = (copy % 1000);
    copy -= millis
    let seconds = ((copy / 1000) % 60);
    copy -= 1000 * seconds
    let minutes = ((copy / 60000) % 60);
    copy -= 60000 * seconds
    let hours = (copy / 3600000);
    copy -= 3600000 * hours
    let days = (copy / 86400000);
    copy -= 86400000 * days
    let weeks = (copy / 604800000);
    copy -= 604800000 * weeks
    let months = (copy / 2419200000);
    copy -= 2419200000 * months;

    if (millis != 0)
        return "Every "+ (millis + seconds * 1000) +" milliseconds";

    else if (seconds != 0)
        return  "Every "+ (seconds + minutes * 60)+" seconds";

    else if (minutes != 0)
        return  "Every "+ (minutes + hours * 60) +" minutes";
    else if (hours != 0)
        return "Every "+ (hours + days * 24) + " hours";
    else if (days != 0)
        return "Every "+ (days + weeks * 7)+ " days";
    else if (weeks != 0)
        return "Every " + (weeks + months * 4) + " weeks";
    else
        return "Every "+ months +" months";
}
