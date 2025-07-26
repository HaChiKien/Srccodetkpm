<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="utf-8">
<title>A simple, clean, and responsive HTML invoice template</title>
<style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f5f6fa;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 650px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
        }

        h2 {
            text-align: center;
            color: #273c75;
        }

        .info {
            margin-top: 30px;
            font-size: 16px;
        }

        .info label {
            font-weight: bold;
            display: block;
            margin-top: 15px;
            color: #353b48;
        }

        .info p {
            margin: 5px 0 15px 0;
            color: #2f3640;
        }

        .payment-section {
            margin-top: 40px;
            text-align: center;
        }

        .btn-vnpay {
            background-color: #d63031;
            border: none;
            color: white;
            padding: 14px 28px;
            font-size: 16px;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .btn-vnpay:hover {
            background-color: #c0392b;
        }

        .note {
            margin-top: 20px;
            font-style: italic;
            color: #718093;
            font-size: 14px;
            text-align: center;
        }
    </style>
<link href="css/style-invoice.css" rel="stylesheet">
</head>

<body>


	<form method="post" action="@{/pay}">
		<div class="invoice-box">
			<table cellpadding="0" cellspacing="0">
				<tr class="top">
					<td colspan="2">
						<table>
							<tr>
								<td class="title"><img src="hinh/iconhome.ico" width="150"
									height="150"></td>

								<td>Invoice #: 0${bookingDTO.roomCode}<br> Created:
									${bookingDTO.checkInDate}<br>
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<tr class="information">
					<td colspan="2">
						<table>
							<tr>
								<td>Homie Hotel, Inc.<br> Phone: 0977525030<br>
									Address: 273 Nguyen Gia Tri, Ward 25,<br> Binh Thanh,
									City. Ho Chi Minh
								</td>

								<td>Dear: ${bookingDTO.name}.<br> Phone:
									${bookingDTO.phoneNumber}<br> Email:${bookingDTO.email}
								</td>
							</tr>
						</table>
					</td>
				</tr>

				<tr class="heading">
					<td>Payment Method</td>

					<td>Money #</td>
				</tr>

				<tr class="details">
					<td>Price Room</td>

					<td>${phong.giaPhong}$</td>
				</tr>

				<tr class="heading">
					<td>Item</td>

					<td>Check</td>
				</tr>


				<tr class="item">
					<td>Room Number</td>

					<td>${phong.soPhong}</td>
				</tr>

				<tr class="item">
					<td>Room Type</td>

					<td>${phong.loaiPhong.tenLoaiPhong}</td>
				</tr>

				<tr class="item">
					<td>Check-in Date</td>

					<td>${bookingDTO.checkInDate}</td>
				</tr>

				<tr class="item last">
					<td>Check-out Date</td>

					<td>${bookingDTO.checkOutDate}</td>
				</tr>
				<tr class="item last">
					<td>QR</td>

					<td><img
						src="${pageContext.request.contextPath}/qrcode/${bookingDTO.name}"
						width="100" height="100"></td>
				</tr>
			</table>
		</div>

	</form>
    <div style="display: flex; justify-content: center; align-items: center; height: 200px;">
        <form action="/vnpay-payment" method="get">
            <button type="submit" style="
    background-color: white;
    color: #0066b2;
    padding: 10px 20px;
    border: 2px solid #0066b2;
    font-size: 16px;
    border-radius: 8px;
    cursor: pointer;
    display: flex;
    align-items: center;
    font-weight: bold;
    transition: all 0.3s ease; " >
    <img src="../../images/vnpay.png" alt="VNPAY" style="height: 20px; margin-right: 10px;">
    Payment with VNPAY
  			</button>
		</form>
		<form action="/onepay-international" method="post">
		<input type="hidden" name="amount" value="${tongTien}">
    	<input type="hidden" name="orderInfo" value="Thanh toán đặt phòng tại HomieHotel">
            <button type="submit" style="
    background-color: white;
    color: #0066b2;
    padding: 10px 20px;
    border: 2px solid #0066b2;
    font-size: 16px;
    border-radius: 8px;
    cursor: pointer;
    display: flex;
    align-items: center;
    font-weight: bold;
    transition: all 0.3s ease; " >
    <img src="../../images/onepay.png" alt="VNPAY" style="height: 20px; margin-right: 10px;">
    Payment with ONEPAY
  			</button>
		</form>
    </div>
    
</body>
</html>
