<?php

defined('BASEPATH') OR exit('No direct script access allowed');
require FCPATH . 'application/libraries/REST_Controller.php';

class User extends REST_Controller {

	function __construct() {
		parent::__construct();
		header('Access-Control-Allow-Origin: *');
		$this->load->model('API/Muser', 'm');
	} 

	public function Signup_post() {
		if (isset($_POST['uFirstName']) && !empty($_POST['uFirstName']) && isset($_POST['uLastName']) && !empty($_POST['uLastName']) && isset($_POST['uMobile']) && !empty($_POST['uMobile']) && isset($_POST['uEmail']) && !empty($_POST['uEmail']) && isset($_POST['uPassword']) && !empty($_POST['uPassword']) && isset($_POST['uImage']) && !empty($_POST['uImage'])) {

			$data = $this->m->getSignup();

			if ($data) {
				$data['status'] = 'true';
			} else {
				$data['status'] = 'false';
			}
		} else {
			$data['status'] = 'false';
		}

		echo json_encode($data); 
	}

	public function Login_post() {
		if (isset($_POST['uEmail']) && !empty($_POST['uEmail']) && isset($_POST['uPassword']) && !empty($_POST['uPassword'])) {
			$data = $this->m->getlogin();
			if ($data) {
				$data['status'] = 'true';
			} else {
				$data['status'] = 'false';
			}
		} else {
			$data['status'] = 'false';
		}

		echo json_encode($data);
	}

	public function Detail_post() {
		if (isset($_POST['uId']) && !empty($_POST['uId'])) {
			$data = $this->m->getDetails(); 
			if ($data) {
				$data['status'] = 'true';
			} else {
				$data['status'] = 'false';
			}
		} else {
			$data['status'] = 'false';
		}

		echo json_encode($data);
	}
	
	public function ProductList_get() {
		if (isset($_GET['sLat']) && !empty($_GET['sLat']) && isset($_GET['sLog']) && !empty($_GET['sLog'])) {

			$data = $this->m->getProductList(); 
			if ($data) {
				$success = $data;
			} else {
				$success['status'] = 'false';
			}
		} else {
			$success['status'] = 'false';
		}

		echo json_encode($success);
	}

	
	public function EditProfile_post() {
		if (isset($_POST['uId']) && !empty($_POST['uId']) && isset($_POST['uFirstName']) && !empty($_POST['uFirstName']) && isset($_POST['uLastName']) && !empty($_POST['uLastName']) && isset($_POST['uMobile']) && !empty($_POST['uMobile'])) {
			
			$data = $this->m->getEditProfile(); 
			if ($data) {
				$data['status'] = 'true';
			} else {
				$data['status'] = 'false';
			}
		} else {
			$data['status'] = 'false';
		}

		echo json_encode($data);
	}

}