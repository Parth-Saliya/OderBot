<?php
defined('BASEPATH') OR exit('No direct script access allowed');

$route['storeSignUp'] = 'API/Store/Signup';
$route['storeLatLng'] = 'API/Store/LatLng';
$route['storeLogin'] = 'API/Store/Login';
$route['storeDetail'] = 'API/Store/Detail';
$route['storeList'] = 'API/Store/List';
$route['storeProductAdd'] = 'API/Store/ProductAdd';
$route['storeProductList'] = 'API/Store/ProductList';
$route['storeProductDelete'] = 'API/Store/ProductDelete';
$route['storeProductEdit'] = 'API/Store/ProductEdit';
$route['storeEditProfile'] = 'API/Store/EditProfile';

$route['storeUserProductList'] = 'API/User/ProductList';

$route['userSignUp'] = 'API/User/Signup';
$route['userLogin'] = 'API/User/Login';
$route['userDetail'] = 'API/User/Detail';
$route['userEditProfile'] = 'API/User/EditProfile'; 

$route['userOrder'] = 'API/Order/Order';
$route['userOrderList'] = 'API/Order/uOrderList';
$route['storeOrderList'] = 'API/Order/sOrderList';
$route['userOrderDetail'] = 'API/Order/uOrderDetail';
$route['storeOrderDetail'] = 'API/Order/sOrderDetail';
$route['userScan'] = 'API/Order/Scan';
$route['userOrderCancel'] = 'API/Order/uOrderCancel';
$route['userReview'] = 'API/Order/Review';
$route['userReviewShow'] = 'API/Order/ReviewShow'; 

$route['default_controller'] = 'welcome';
$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;
