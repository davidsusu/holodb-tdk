<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Employee extends Model
{
    /** @use HasFactory<\Database\Factories\EmployeeFactory> */
    use HasFactory;

    public $timestamps = false;

    protected $fillable = [
        'company_id',
        'firstname',
        'lastname',
        'birth_year',
        'birth_country',
        'preferred_color',
    ];
}
