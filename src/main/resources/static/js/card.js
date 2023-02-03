$(document).ready( function() {

    $(document).ready(function() {
        $(window).keydown(function(event){
            if(event.keyCode == 13) {
                event.preventDefault();
                return false;
            }
        });
    });

    $("#wizard1").simpleWizard({
        cssClassStepActive: "active",
        cssClassStepDone: "done",
        onFinish: function() {
            //alert("Wizard finished");
        }
    });
    $(".phoneNumber").mask("+7 (999) 999-9999");
    $(".phoneNumber2").mask("8 (999) 999-9999");
    $(".datepicker").mask("99.99.9999");

    //$(".paspSeries").mask("9999");
    //$(".paspNumber").mask("999999");
    $(".pasportCodeDevision").mask("999-999");
    //$(".zipCode").mask("999999");

    <!--активация  тарифов пенсионный/gold luxe -->
    $('input[name="cardType"]').click(function(){

        $('#tariff :input').removeAttr('disabled');

        if ($('#card_type_standart:checked').length > 0) {
            $('#tariff_pension').show('slow');
            if ($('#card_tariff_pension:checked').length > 0) {
                $('#pension_condition').show('slow');
            } else {
                $('#card_tariff_standart').prop('checked', true);
                $('#placeOfWorkLabel').text("Место работы, учебы:");
                $('#placeOfWorkInput').show('slow');
                $('#placeOfWorkSelectList').hide('slow');
                $('#placeOfWork').show('slow');
            }

        } else {
            $('#tariff_pension').hide('slow');
            $('#pension_condition').hide('slow');
        }

        if ($('#card_type_gold:checked').length > 0) {
            $('#tariff_goldluxe').show('slow');
            $('#pens_voen_bilet_doc').hide('slow');
            if ($('#card_tariff_pension:checked').length > 0) {
                $('#card_tariff_standart').prop('checked', true);
                $('#placeOfWorkLabel').text("Место работы, учебы:");
                $('#placeOfWorkInput').show('slow');
                $('#placeOfWorkSelectList').hide('slow');
                $('#placeOfWork').show('slow');
            }
        } else {
            $('#tariff_goldluxe').hide('slow');
        }

    });

    <!--активация  пенсионных условий -->
    $('input[name="cardTariff"]').click(function() {
        if ($('#card_tariff_pension:checked').length > 0) {
            $('#pension_condition').show('slow');

            if ($('#card_clause_1:checked').length > 0) {
                $('#placeOfWorkLabel').text("Место работы, учебы:");
                $('#placeOfWorkInput').show('slow');
                $('#placeOfWorkSelectList').hide('slow');
                $('#placeOfWork').show('slow');
            }
            else
                $('#placeOfWork').hide('slow');

            if ($('#card_clause_2:checked').length > 0 || $('#card_clause_3:checked').length > 0) {
                $('#pens_voen_bilet_doc').show('slow');
                if($('#card_clause_2:checked').length > 0) {
                    $('#pens_voen_bilet_doc_title').text("Военный билет (документ, удостоверяющий личность военослужащего)");
                }
                if($('#card_clause_3:checked').length > 0) {
                    $('#pens_voen_bilet_doc_title').text("Пенсионное удостоверение");
                }
            } else {
                $('#pens_voen_bilet_doc').hide('slow');
            }
        } else {
            if ($('#card_tariff_zp:checked').length > 0) {
                $('#placeOfWorkLabel').text("Укажите место работы (выбрать из списка):");
                $('#placeOfWorkSelectList').show('slow');
                $('#placeOfWorkInput').hide('slow');
                $('#placeOfWorkSelectList').val("");
            } else {
                $('#placeOfWorkLabel').text("Место работы, учебы:");
                $('#placeOfWorkInput').show('slow');
                $('#placeOfWorkSelectList').hide('slow');
                $('#placeOfWorkInput').val("");
            }
            $('#pension_condition').hide('slow');
            $('#pens_voen_bilet_doc').hide('slow');
            $('#placeOfWork').show('slow');
        }
    });

    <!--активация  почтового адреса -->
    $('#check_post_address').click(function() {
        if( $(this).is(':checked')) {
            $('#post_address').hide('slow');
        } else {
            $('#post_address').show('slow');
        }
    });

    <!--активация  военного билета/пенсионного удостоверения -->
    $('input[name="cardClause"]').click(function() {

        if ($('#card_clause_1:checked').length > 0) {
            $('#placeOfWorkLabel').text("Место работы, учебы:");
            $('#placeOfWorkInput').show('slow');
            $('#placeOfWorkSelectList').hide('slow');
            $('#placeOfWork').show('slow');
        }
        else
            $('#placeOfWork').hide('slow');

        if (($('#card_clause_2:checked').length > 0 || $('#card_clause_3:checked').length > 0) && $('#card_tariff_pension:checked').length > 0) {
            $('#pens_voen_bilet_doc').show('slow');
            if($('#card_clause_2:checked').length > 0) {
                $('#pens_voen_bilet_doc_title').text("Военный билет (документ, удостоверяющий личность военослужащего)");
            }
            if($('#card_clause_3:checked').length > 0) {
                $('#pens_voen_bilet_doc_title').text("Пенсионное удостоверение");
            }
        } else {
            $('#pens_voen_bilet_doc').hide('slow');
        }
    });

    <!--отправка персональных данных на форму  -->
    $('#agreeWithPersonalInformation').click(function() {
        $('#personalFIO').text("Я, " + $('#lastName').val() + " " + $('#firstName').val() + " " + $('#secondName').val());

        $('#personalAddressLocation').text("зарегистрированный по адресу: " +
            $('#inputZipCode1').val() + ", " + $('#inputProvince1').val() + "," + $('#inputDistrict1').val() + "," + $('#inputDistrict1').val() + "," + $('#inputCity1').val() + "," + $('#inputStreet1').val() + "," + $('#inputHouse1').val() + ","
            + $('#inputHousing1').val() + "," + $('#inputApartment1').val() + ",");

        if ($('#check_post_address:checked').length > 0) {
            $('#personalAddressPostal').text("проживающий по адресу: " +
                $('#inputZipCode1').val() + ", " + $('#inputProvince1').val() + "," + $('#inputDistrict1').val() + "," + $('#inputDistrict1').val() + "," + $('#inputCity1').val() + "," + $('#inputStreet1').val() + "," + $('#inputHouse1').val() + ","
                + $('#inputHousing1').val() + "," + $('#inputApartment1').val() + ",");
        } else {
            $('#personalAddressPostal').text("проживающий по адресу: " +
                $('#inputZipCode2').val() + ", " + $('#inputProvince2').val() + "," + $('#inputDistrict2').val() + "," + $('#inputDistrict2').val() + "," + $('#inputCity2').val() + "," + $('#inputStreet2').val() + "," + $('#inputHouse2').val() + ","
                + $('#inputHousing2').val() + "," + $('#inputApartment2').val() + ",");
        }

        $('#personalPassport').text("документ, удостоверяющий личность: паспорт серия " + $('#inputPasportSeries').val() + " номер " + $('#inputPasportNumber').val());
        $('#personalPassportIssued').text("выдан " + $('#inputPasportIssued').val() + " " + $('#inputPasportIssuedBy').val() + ", код подразделения " + $('#inputPasportCodeDevision').val());
    });

});