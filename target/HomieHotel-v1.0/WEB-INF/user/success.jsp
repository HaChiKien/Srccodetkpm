<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .card {
            background: white;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            max-width: 500px;
            text-align: center;
        }

        .card h1 {
            color: #28a745;
            margin-bottom: 20px;
        }

        .card p {
            font-size: 18px;
            margin-bottom: 15px;
        }

        .btn {
            background-color: #28a745;
            color: white;
            padding: 10px 20px;
            border: none;
            font-size: 16px;
            border-radius: 6px;
            text-decoration: none;
            cursor: pointer;
        }

        .btn:hover {
            background-color: #218838;
        }

        .icon {
            font-size: 60px;
            color: #28a745;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div class="card">
    <div class="icon"><img  src="../../hinh/check.png" width="150" height="150"/></div>
<h1>Paid Successfull</h1>
<h2>Thank for your payment</h2>
<h4><a style=color:Green>Payper</a> ${bookingDTO.name}</h4>
<h4><a style=color:Green>Room:</a> ${phong.loaiPhong.tenLoaiPhong} ${phong.soPhong}</h4>
<h4><a style=color:Green>Total:</a> ${phong.giaPhong}$</h4>
<a href="home" class="btn">GO HOME</a>
</body>
</html>