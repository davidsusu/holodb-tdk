<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class DatabaseSeeder extends Seeder
{
    use WithoutModelEvents;

    public function run(): void
    {
        DB::statement('SET FOREIGN_KEY_CHECKS = 0');
        $this->call([
            CompanySeeder::class,
            EmployeeSeeder::class,
            CouponSeeder::class,
        ]);
        DB::statement('SET FOREIGN_KEY_CHECKS = 1');
    }
}
