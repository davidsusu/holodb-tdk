<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

use App\Models\Company;

class CompanySeeder extends Seeder
{
    public function run(): void
    {
        DB::statement('ALTER TABLE companies DISABLE KEYS');
        Company::factory()->count(200)->create();
        DB::statement('ALTER TABLE companies ENABLE KEYS');
    }
}
