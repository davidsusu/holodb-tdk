<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

use App\Models\Company;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('employees', function (Blueprint $table) {
            $table->id();
            $table->foreignIdFor(Company::class)->constrained()->cascadeOnUpdate()->cascadeOnDelete();
            $table->string('firstname');
            $table->string('lastname');
            $table->year('birth_year');
            $table->string('birth_country');
            $table->string('preferred_color');
            $table->index('firstname', 'idx_employees_firstname');
            $table->index('lastname', 'idx_employees_lastname');
            $table->index('birth_year', 'idx_employees_birth_year');
            $table->index('birth_country', 'idx_employees_birth_country');
            $table->index('preferred_color', 'idx_employees_preferred_color');
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('employees');
    }
};
